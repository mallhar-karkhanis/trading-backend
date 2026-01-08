package com.bajajbroking.trading.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Tradable financial instrument")
public record InstrumentDTO(
    @Schema(description = "Trading symbol", example = "RELIANCE")
    String symbol,
    
    @Schema(description = "Exchange code", example = "NSE")
    String exchange,
    
    @Schema(description = "Instrument type", example = "EQUITY")
    String instrumentType,
    
    @Schema(description = "Last traded price", example = "2456.75")
    BigDecimal lastTradedPrice
) {}