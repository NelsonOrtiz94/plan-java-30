package com.bancolombia.orders.domain.model;

import com.bancolombia.orders.domain.exception.InvalidQuantityException;

import java.util.Objects;

public class OrderItem {
    private final String productId;
    private final int quantity;
    private final Money unitPrice;

    public OrderItem(String productId, int quantity, Money unitPrice) {
        Objects.requireNonNull(productId, "productId no puede ser nulo");
        Objects.requireNonNull(unitPrice, "unitPrice no puede ser nulo");
        if (productId.isBlank()) throw new IllegalArgumentException("productId no puede estar vacio");
        if (quantity <= 0) throw new InvalidQuantityException(productId, quantity);
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }
}