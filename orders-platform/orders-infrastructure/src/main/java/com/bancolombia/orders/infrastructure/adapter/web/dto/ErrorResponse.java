package com.bancolombia.orders.infrastructure.adapter.web.dto;

public record ErrorResponse(String code, String message, String traceId) {
}