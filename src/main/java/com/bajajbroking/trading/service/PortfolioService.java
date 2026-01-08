package com.bajajbroking.trading.service;

import com.bajajbroking.trading.dto.PortfolioDTO;
import com.bajajbroking.trading.entity.Order;
import com.bajajbroking.trading.entity.Portfolio;
import com.bajajbroking.trading.exception.InsufficientHoldingsException;
import com.bajajbroking.trading.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    
    private static final String DEMO_USER = "demo@trade.com";
    
    @Transactional(readOnly = true)
    public List<PortfolioDTO> getUserPortfolio() {
        log.info("Fetching portfolio for user: {}", DEMO_USER);
        return portfolioRepository.findByUserId(DEMO_USER)
            .stream()
            .filter(p -> p.getQuantity() > 0)
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public void validateSellOrder(String userId, String symbol, int quantity) {
        Optional<Portfolio> holding = portfolioRepository.findByUserIdAndSymbol(userId, symbol);
        
        if (holding.isEmpty() || holding.get().getQuantity() < quantity) {
            int available = holding.map(Portfolio::getQuantity).orElse(0);
            throw new InsufficientHoldingsException(symbol, available, quantity);
        }
    }
    
    @Transactional
    public void updatePortfolio(String userId, String symbol, String exchange, 
                                Order.OrderSide side, int quantity, BigDecimal price) {
        Optional<Portfolio> existingHolding = portfolioRepository.findByUserIdAndSymbol(userId, symbol);
        
        if (side == Order.OrderSide.BUY) {
            handleBuy(userId, symbol, exchange, quantity, price, existingHolding);
        } else {
            handleSell(userId, symbol, quantity, price, existingHolding);
        }
    }
    
    private void handleBuy(String userId, String symbol, String exchange,
                          int quantity, BigDecimal price,
                          Optional<Portfolio> existingHolding) {
        if (existingHolding.isPresent()) {
            Portfolio holding = existingHolding.get();
            
            BigDecimal currentValue = holding.getAveragePrice()
                .multiply(new BigDecimal(holding.getQuantity()));
            BigDecimal newValue = price.multiply(new BigDecimal(quantity));
            BigDecimal totalValue = currentValue.add(newValue);
            
            int newQuantity = holding.getQuantity() + quantity;
            BigDecimal newAvgPrice = totalValue
                .divide(new BigDecimal(newQuantity), 2, RoundingMode.HALF_UP);
            
            holding.setQuantity(newQuantity);
            holding.setAveragePrice(newAvgPrice);
            holding.setCurrentValue(newAvgPrice.multiply(new BigDecimal(newQuantity)));
            
            portfolioRepository.save(holding);
            log.info("Updated holding: {} - Qty: {}, Avg: {}", 
                     symbol, newQuantity, newAvgPrice);
        } else {
            BigDecimal currentValue = price.multiply(new BigDecimal(quantity));
            
            Portfolio newHolding = Portfolio.builder()
                .userId(userId)
                .symbol(symbol)
                .exchange(exchange)
                .quantity(quantity)
                .averagePrice(price)
                .currentValue(currentValue)
                .build();
            
            portfolioRepository.save(newHolding);
            log.info("Created new holding: {} - Qty: {}, Avg: {}", 
                     symbol, quantity, price);
        }
    }
    
    private void handleSell(String userId, String symbol, int quantity,
                           BigDecimal price, Optional<Portfolio> existingHolding) {
        if (existingHolding.isEmpty()) {
            throw new InsufficientHoldingsException(symbol, 0, quantity);
        }
        
        Portfolio holding = existingHolding.get();
        int newQuantity = holding.getQuantity() - quantity;
        
        if (newQuantity < 0) {
            throw new InsufficientHoldingsException(
                symbol, holding.getQuantity(), quantity
            );
        }
        
        holding.setQuantity(newQuantity);
        
        if (newQuantity > 0) {
            holding.setCurrentValue(
                holding.getAveragePrice().multiply(new BigDecimal(newQuantity))
            );
        } else {
            holding.setAveragePrice(BigDecimal.ZERO);
            holding.setCurrentValue(BigDecimal.ZERO);
        }
        
        portfolioRepository.save(holding);
        log.info("Updated holding after sell: {} - Qty: {}", symbol, newQuantity);
    }
    
    private PortfolioDTO toDTO(Portfolio portfolio) {
        return new PortfolioDTO(
            portfolio.getSymbol(),
            portfolio.getExchange(),
            portfolio.getQuantity(),
            portfolio.getAveragePrice(),
            portfolio.getCurrentValue()
        );
    }
}