package com.bajajbroking.trading.dto;

import com.bajajbroking.trading.entity.Order.OrderSide;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Executed trade details")
public record TradeDTO(
    @Schema(description = "Trade ID", example = "1")
    Long tradeId,
    
    @Schema(description = "Order ID", example = "1")
    Long orderId,
    
    @Schema(description = "Trading symbol", example = "RELIANCE")
    String symbol,
    
    @Schema(description = "Exchange code", example = "NSE")
    String exchange,
    
    @Schema(description = "Trade side", example = "BUY")
    OrderSide side,
    
    @Schema(description = "Trade quantity", example = "10")
    Integer quantity,
    
    @Schema(description = "Execution price", example = "2456.75")
    BigDecimal executedPrice,
    
    @Schema(description = "Total trade value", example = "24567.50")
    BigDecimal totalValue,
    
    @Schema(description = "Execution time")
    LocalDateTime executedAt
) {}