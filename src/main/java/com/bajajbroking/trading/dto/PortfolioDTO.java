package com.bajajbroking.trading.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "User portfolio holding")
public record PortfolioDTO(
    @Schema(description = "Trading symbol", example = "RELIANCE")
    String symbol,
    
    @Schema(description = "Exchange code", example = "NSE")
    String exchange,
    
    @Schema(description = "Holding quantity", example = "10")
    Integer quantity,
    
    @Schema(description = "Average purchase price", example = "2456.75")
    BigDecimal averagePrice,
    
    @Schema(description = "Current market value", example = "24567.50")
    BigDecimal currentValue
) {}