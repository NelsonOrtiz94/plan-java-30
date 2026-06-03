package com.bancolombia.orders.domain.exception;
public class EmptyOrderException extends OrderDomainException {
    public EmptyOrderException() {
        super("ORDER_EMPTY", "La orden debe contener al menos un producto.");
    }
}
