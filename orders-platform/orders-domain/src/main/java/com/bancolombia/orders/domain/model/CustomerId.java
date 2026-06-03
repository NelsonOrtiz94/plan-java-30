package com.bancolombia.orders.domain.model;

import java.util.Objects;

public final class CustomerId {
    private final String value;

    private CustomerId(String value) {
        Objects.requireNonNull(value, "CustomerId no puede ser nulo");
        if (value.isBlank()) throw new IllegalArgumentException("CustomerId no puede estar vacio");
        this.value = value;
    }

    public static CustomerId of(String value) {
        return new CustomerId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerId c)) return false;
        return Objects.equals(value, c.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}