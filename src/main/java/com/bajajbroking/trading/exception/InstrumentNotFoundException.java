package com.bajajbroking.trading.exception;

public class InstrumentNotFoundException extends RuntimeException {
    
    public InstrumentNotFoundException(String message) {
        super(message);
    }
    
    public InstrumentNotFoundException(String symbol, String exchange) {
        super(String.format("Instrument not found: %s on %s", symbol, exchange));
    }
}