package com.bajajbroking.trading.service;

import com.bajajbroking.trading.dto.InstrumentDTO;
import com.bajajbroking.trading.entity.Instrument;
import com.bajajbroking.trading.exception.InstrumentNotFoundException;
import com.bajajbroking.trading.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InstrumentService {
    
    private final InstrumentRepository instrumentRepository;
    
    public List<InstrumentDTO> getAllInstruments() {
        log.info("Fetching all instruments");
        return instrumentRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public Instrument getInstrument(String symbol, String exchange) {
        return instrumentRepository.findBySymbolAndExchange(symbol, exchange)
            .orElseThrow(() -> new InstrumentNotFoundException(symbol, exchange));
    }
    
    private InstrumentDTO toDTO(Instrument instrument) {
        return new InstrumentDTO(
            instrument.getSymbol(),
            instrument.getExchange(),
            instrument.getInstrumentType(),
            instrument.getLastTradedPrice()
        );
    }
}