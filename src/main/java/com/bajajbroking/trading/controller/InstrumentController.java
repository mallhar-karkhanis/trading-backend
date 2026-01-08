package com.bajajbroking.trading.controller;

import com.bajajbroking.trading.dto.InstrumentDTO;
import com.bajajbroking.trading.service.InstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instruments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Instruments", description = "Tradable instruments API")
public class InstrumentController {
    
    private final InstrumentService instrumentService;
    
    @GetMapping
    @Operation(
        summary = "List all tradable instruments",
        description = "Returns list of all available instruments for trading"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    public ResponseEntity<List<InstrumentDTO>> getAllInstruments() {
        log.info("GET /api/v1/instruments - List all instruments");
        List<InstrumentDTO> instruments = instrumentService.getAllInstruments();
        return ResponseEntity.ok(instruments);
    }
}