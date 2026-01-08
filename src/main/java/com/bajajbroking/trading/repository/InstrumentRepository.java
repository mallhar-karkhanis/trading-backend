package com.bajajbroking.trading.repository;

import com.bajajbroking.trading.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    
    Optional<Instrument> findBySymbolAndExchange(String symbol, String exchange);
    
    boolean existsBySymbolAndExchange(String symbol, String exchange);
}