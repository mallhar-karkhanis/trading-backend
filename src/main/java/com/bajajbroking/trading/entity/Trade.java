package com.bajajbroking.trading.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 20)
    private String symbol;
    
    @Column(nullable = false, length = 10)
    private String exchange;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Order.OrderSide side;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal executedPrice;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalValue;
    
    @Column(nullable = false)
    private LocalDateTime executedAt;
    
    @PrePersist
    protected void onCreate() {
        executedAt = LocalDateTime.now();
        if (totalValue == null && executedPrice != null && quantity != null) {
            totalValue = executedPrice.multiply(new BigDecimal(quantity));
        }
    }
}