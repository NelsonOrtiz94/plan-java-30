package com.bancolombia.orders.infrastructure.adapter.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotBlank(message = "El campo productId es requerido.") String productId,
        @Positive(message = "La cantidad debe ser mayor a cero.") int quantity,
        @Positive(message = "El precio unitario debe ser mayor a cero.") BigDecimal unitPrice
) {
}