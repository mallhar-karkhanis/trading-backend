package com.bajajbroking.trading.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 20)
    private String symbol;
    
    @Column(nullable = false, length = 10)
    private String exchange;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderSide side;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderType type;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private OrderStatus status;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal executedPrice;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime executedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.NEW;
        }
    }
    
    public enum OrderSide {
        BUY, SELL
    }
    
    public enum OrderType {
        MARKET, LIMIT
    }
    
    public enum OrderStatus {
        NEW, PLACED, EXECUTED, CANCELLED
    }
}