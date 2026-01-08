package com.bajajbroking.trading.exception;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(Long orderId) {
        super(String.format("Order not found: %d", orderId));
    }
}