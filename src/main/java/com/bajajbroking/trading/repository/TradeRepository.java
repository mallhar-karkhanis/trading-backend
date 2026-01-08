package com.bajajbroking.trading.repository;

import com.bajajbroking.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    
    List<Trade> findByUserIdOrderByExecutedAtDesc(String userId);
}