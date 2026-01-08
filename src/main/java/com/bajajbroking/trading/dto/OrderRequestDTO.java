package com.bajajbroking.trading.dto;

import com.bajajbroking.trading.entity.Order.OrderSide;
import com.bajajbroking.trading.entity.Order.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Request to place a trading order")
public record OrderRequestDTO(
    @NotBlank(message = "Symbol is required")
    @Schema(description = "Trading symbol", example = "RELIANCE", required = true)
    String symbol,
    
    @NotNull(message = "Order side is required")
    @Schema(description = "Order side", example = "BUY", required = true)
    OrderSide side,
    
    @NotNull(message = "Order type is required")
    @Schema(description = "Order type", example = "MARKET", required = true)
    OrderType type,
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @Schema(description = "Order quantity", example = "10", required = true)
    Integer quantity,
    
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Limit price (required for LIMIT orders)", 
            example = "2450.00")
    BigDecimal price
) {}