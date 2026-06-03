package com.bancolombia.orders.infrastructure.adapter.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "El campo customerId es requerido.") String customerId,
        @NotEmpty(message = "La orden debe tener al menos un item.") @Valid List<OrderItemRequest> items
) {
}