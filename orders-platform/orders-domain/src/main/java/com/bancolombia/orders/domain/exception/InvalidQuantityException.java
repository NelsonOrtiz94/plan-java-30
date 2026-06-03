package com.bancolombia.orders.domain.exception;

public class InvalidQuantityException extends OrderDomainException {
    public InvalidQuantityException(String productId, int quantity) {
        super("INVALID_QUANTITY",
                String.format("La cantidad para '%s' debe ser mayor a cero. Recibido: %d", productId, quantity));
    }
}