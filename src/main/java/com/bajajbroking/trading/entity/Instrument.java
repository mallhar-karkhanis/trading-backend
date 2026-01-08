package com.bajajbroking.trading.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "instruments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"symbol", "exchange"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instrument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 20)
    private String symbol;
    
    @Column(nullable = false, length = 10)
    private String exchange;
    
    @Column(nullable = false, length = 20)
    private String instrumentType;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal lastTradedPrice;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}