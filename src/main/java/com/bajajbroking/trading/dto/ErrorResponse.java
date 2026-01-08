package com.bajajbroking.trading.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    @Schema(description = "HTTP status code", example = "400")
    int status,
    
    @Schema(description = "Error message", example = "Invalid order request")
    String message,
    
    @Schema(description = "Detailed error information")
    String details,
    
    @Schema(description = "Validation errors")
    Map<String, String> validationErrors,
    
    @Schema(description = "Timestamp")
    LocalDateTime timestamp,
    
    @Schema(description = "Request path", example = "/api/v1/orders")
    String path
) {
    public ErrorResponse(int status, String message, String path) {
        this(status, message, null, null, LocalDateTime.now(), path);
    }
    
    public ErrorResponse(int status, String message, String details, String path) {
        this(status, message, details, null, LocalDateTime.now(), path);
    }
    
    public ErrorResponse(int status, String message, Map<String, String> validationErrors, String path) {
        this(status, message, null, validationErrors, LocalDateTime.now(), path);
    }
}