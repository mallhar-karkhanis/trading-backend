package com.bajajbroking.trading.dto;

import com.bajajbroking.trading.entity.Order.OrderSide;
import com.bajajbroking.trading.entity.Order.OrderStatus;
import com.bajajbroking.trading.entity.Order.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Order details")
public record OrderResponseDTO(
    @Schema(description = "Order ID", example = "1")
    Long orderId,
    
    @Schema(description = "Trading symbol", example = "RELIANCE")
    String symbol,
    
    @Schema(description = "Exchange code", example = "NSE")
    String exchange,
    
    @Schema(description = "Order side", example = "BUY")
    OrderSide side,
    
    @Schema(description = "Order type", example = "MARKET")
    OrderType type,
    
    @Schema(description = "Order quantity", example = "10")
    Integer quantity,
    
    @Schema(description = "Limit price", example = "2450.00")
    BigDecimal price,
    
    @Schema(description = "Order status", example = "NEW")
    OrderStatus status,
    
    @Schema(description = "Executed price", example = "2456.75")
    BigDecimal executedPrice,
    
    @Schema(description = "Order creation time")
    LocalDateTime createdAt,
    
    @Schema(description = "Order execution time")
    LocalDateTime executedAt
) {}