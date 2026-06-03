package com.bancolombia.orders.infrastructure.adapter.web.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal
) {
}