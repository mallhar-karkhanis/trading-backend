package com.bajajbroking.trading.controller;

import com.bajajbroking.trading.dto.TradeDTO;
import com.bajajbroking.trading.service.TradeService;
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
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Trades", description = "Executed trades API")
public class TradeController {
    
    private final TradeService tradeService;
    
    @GetMapping
    @Operation(
        summary = "Get user's executed trades",
        description = "Returns list of all executed trades for the user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    public ResponseEntity<List<TradeDTO>> getUserTrades() {
        log.info("GET /api/v1/trades - Get user trades");
        List<TradeDTO> trades = tradeService.getUserTrades();
        return ResponseEntity.ok(trades);
    }
}