package com.bajajbroking.trading.repository;

import com.bajajbroking.trading.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(String userId);
    
    Optional<Portfolio> findByUserIdAndSymbol(String userId, String symbol);
}