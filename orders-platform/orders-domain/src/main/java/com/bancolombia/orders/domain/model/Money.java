package com.bancolombia.orders.domain.model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
public final class Money {
    public static final Money ZERO = Money.of(BigDecimal.ZERO);
    private final BigDecimal amount;
    private Money(BigDecimal amount) {
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El monto no puede ser negativo: " + amount);
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
    public static Money of(BigDecimal amount) { return new Money(amount); }
    public static Money of(double amount) { return new Money(BigDecimal.valueOf(amount)); }
    public BigDecimal getAmount() { return amount; }
    public Money add(Money other) { return new Money(this.amount.add(other.amount)); }
    public Money multiply(int factor) { return new Money(this.amount.multiply(BigDecimal.valueOf(factor))); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money m)) return false;
        return Objects.equals(amount, m.amount);
    }
    @Override public int hashCode() { return Objects.hash(amount); }
    @Override public String toString() { return amount.toPlainString(); }
}