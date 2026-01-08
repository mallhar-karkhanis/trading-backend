package com.bajajbroking.trading.service;

import com.bajajbroking.trading.dto.OrderRequestDTO;
import com.bajajbroking.trading.dto.OrderResponseDTO;
import com.bajajbroking.trading.entity.Instrument;
import com.bajajbroking.trading.entity.Order;
import com.bajajbroking.trading.entity.Order.OrderStatus;
import com.bajajbroking.trading.entity.Order.OrderType;
import com.bajajbroking.trading.exception.InvalidOrderException;
import com.bajajbroking.trading.exception.OrderNotFoundException;
import com.bajajbroking.trading.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InstrumentService instrumentService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    
    private static final String DEMO_USER = "demo@trade.com";
    
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        log.info("Placing order: {} {} {} @ {}", 
                 request.side(), request.quantity(), 
                 request.symbol(), request.price());
        
        validateOrder(request);
        
        Instrument instrument = instrumentService.getInstrument(request.symbol(), "NSE");
        
        Order order = Order.builder()
            .userId(DEMO_USER)
            .symbol(request.symbol())
            .exchange(instrument.getExchange())
            .side(request.side())
            .type(request.type())
            .quantity(request.quantity())
            .price(request.price())
            .status(OrderStatus.NEW)
            .build();
        
        order = orderRepository.save(order);
        log.info("Order created with ID: {}", order.getId());
        
        order.setStatus(OrderStatus.PLACED);
        order = orderRepository.save(order);
        
        processOrderExecution(order, instrument);
        
        return toDTO(order);
    }
    
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrder(Long orderId) {
        log.info("Fetching order: {}", orderId);
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        return toDTO(order);
    }
    
    private void validateOrder(OrderRequestDTO request) {
        if (request.type() == OrderType.LIMIT && request.price() == null) {
            throw new InvalidOrderException("Price is required for LIMIT orders");
        }
        
        if (request.side() == Order.OrderSide.SELL) {
            portfolioService.validateSellOrder(DEMO_USER, request.symbol(), request.quantity());
        }
    }
    
    @Transactional
    public void processOrderExecution(Order order, Instrument instrument) {
        BigDecimal executionPrice = null;
        boolean shouldExecute = false;
        
        if (order.getType() == OrderType.MARKET) {
            executionPrice = instrument.getLastTradedPrice();
            shouldExecute = true;
            log.info("MARKET order will execute at LTP: {}", executionPrice);
        } else if (order.getType() == OrderType.LIMIT) {
            BigDecimal limitPrice = order.getPrice();
            BigDecimal currentPrice = instrument.getLastTradedPrice();
            
            if (order.getSide() == Order.OrderSide.BUY && 
                currentPrice.compareTo(limitPrice) <= 0) {
                executionPrice = currentPrice;
                shouldExecute = true;
                log.info("BUY LIMIT order will execute: {} <= {}", 
                         currentPrice, limitPrice);
            } else if (order.getSide() == Order.OrderSide.SELL && 
                       currentPrice.compareTo(limitPrice) >= 0) {
                executionPrice = currentPrice;
                shouldExecute = true;
                log.info("SELL LIMIT order will execute: {} >= {}", 
                         currentPrice, limitPrice);
            }
        }
        
        if (shouldExecute) {
            executeOrder(order, executionPrice);
        } else {
            log.info("Order {} not executed - price conditions not met", order.getId());
        }
    }
    
    private void executeOrder(Order order, BigDecimal executionPrice) {
        order.setStatus(OrderStatus.EXECUTED);
        order.setExecutedPrice(executionPrice);
        order.setExecutedAt(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("Order {} EXECUTED at price {}", order.getId(), executionPrice);
        
        tradeService.createTrade(order, executionPrice);
        
        portfolioService.updatePortfolio(
            order.getUserId(),
            order.getSymbol(),
            order.getExchange(),
            order.getSide(),
            order.getQuantity(),
            executionPrice
        );
    }
    
    private OrderResponseDTO toDTO(Order order) {
        return new OrderResponseDTO(
            order.getId(),
            order.getSymbol(),
            order.getExchange(),
            order.getSide(),
            order.getType(),
            order.getQuantity(),
            order.getPrice(),
            order.getStatus(),
            order.getExecutedPrice(),
            order.getCreatedAt(),
            order.getExecutedAt()
        );
    }
}