package com.bajajbroking.trading.controller;

import com.bajajbroking.trading.dto.PortfolioDTO;
import com.bajajbroking.trading.service.PortfolioService;
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
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio", description = "User holdings API")
public class PortfolioController {
    
    private final PortfolioService portfolioService;
    
    @GetMapping
    @Operation(
        summary = "Get user's portfolio",
        description = "Returns list of all holdings in user's portfolio"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    public ResponseEntity<List<PortfolioDTO>> getUserPortfolio() {
        log.info("GET /api/v1/portfolio - Get user portfolio");
        List<PortfolioDTO> portfolio = portfolioService.getUserPortfolio();
        return ResponseEntity.ok(portfolio);
    }
}