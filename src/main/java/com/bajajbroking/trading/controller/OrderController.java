package com.bajajbroking.trading.controller;

import com.bajajbroking.trading.dto.OrderRequestDTO;
import com.bajajbroking.trading.dto.OrderResponseDTO;
import com.bajajbroking.trading.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "Trading orders API")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    @Operation(
        summary = "Place a new order",
        description = "Place a BUY/SELL order for a tradable instrument"
    )
    @ApiResponse(responseCode = "201", description = "Order created")
    @ApiResponse(responseCode = "400", description = "Invalid order request")
    @ApiResponse(responseCode = "404", description = "Instrument not found")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @Valid @RequestBody OrderRequestDTO request) {
        log.info("POST /api/v1/orders - Place order: {}", request);
        OrderResponseDTO response = orderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{orderId}")
    @Operation(
        summary = "Get order status",
        description = "Retrieve order details and current status by order ID"
    )
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        log.info("GET /api/v1/orders/{} - Get order", orderId);
        OrderResponseDTO response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }
}