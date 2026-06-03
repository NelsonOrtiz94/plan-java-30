package com.bancolombia.orders.domain.exception;

public class OrderDomainException extends RuntimeException {
    private final String code;

    public OrderDomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}