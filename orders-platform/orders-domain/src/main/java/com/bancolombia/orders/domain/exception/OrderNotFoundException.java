package com.bancolombia.orders.domain.exception;

public class OrderNotFoundException extends OrderDomainException {
    public OrderNotFoundException(String orderId) {
        super("ORDER_NOT_FOUND",
                String.format("La orden con ID '%s' no existe.", orderId));
    }
}