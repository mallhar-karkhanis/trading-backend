package com.bajajbroking.trading.service;

import com.bajajbroking.trading.dto.TradeDTO;
import com.bajajbroking.trading.entity.Order;
import com.bajajbroking.trading.entity.Trade;
import com.bajajbroking.trading.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {
    
    private final TradeRepository tradeRepository;
    
    private static final String DEMO_USER = "demo@trade.com";
    
    @Transactional(readOnly = true)
    public List<TradeDTO> getUserTrades() {
        log.info("Fetching trades for user: {}", DEMO_USER);
        return tradeRepository.findByUserIdOrderByExecutedAtDesc(DEMO_USER)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public Trade createTrade(Order order, BigDecimal executionPrice) {
        BigDecimal totalValue = executionPrice.multiply(new BigDecimal(order.getQuantity()));
        
        Trade trade = Trade.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .symbol(order.getSymbol())
            .exchange(order.getExchange())
            .side(order.getSide())
            .quantity(order.getQuantity())
            .executedPrice(executionPrice)
            .totalValue(totalValue)
            .build();
        
        trade = tradeRepository.save(trade);
        log.info("Trade created: {} {} @ {} = {}", 
                 order.getSide(), order.getQuantity(), 
                 executionPrice, totalValue);
        
        return trade;
    }
    
    private TradeDTO toDTO(Trade trade) {
        return new TradeDTO(
            trade.getId(),
            trade.getOrderId(),
            trade.getSymbol(),
            trade.getExchange(),
            trade.getSide(),
            trade.getQuantity(),
            trade.getExecutedPrice(),
            trade.getTotalValue(),
            trade.getExecutedAt()
        );
    }
}