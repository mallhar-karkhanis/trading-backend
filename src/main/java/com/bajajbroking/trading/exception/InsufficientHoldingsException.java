package com.bajajbroking.trading.exception;

public class InsufficientHoldingsException extends RuntimeException {
    
    public InsufficientHoldingsException(String message) {
        super(message);
    }
    
    public InsufficientHoldingsException(String symbol, int available, int requested) {
        super(String.format(
            "Insufficient holdings for %s. Available: %d, Requested: %d",
            symbol, available, requested
        ));
    }
}