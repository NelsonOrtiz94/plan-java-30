package com.bancolombia.orders.domain.model;

import com.bancolombia.orders.domain.exception.EmptyOrderException;
import com.bancolombia.orders.domain.exception.OrderDomainException;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Money total;
    private final Instant createdAt;

    private Order(String orderId, CustomerId customerId, List<OrderItem> items,
                  OrderStatus status, Instant createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = Collections.unmodifiableList(items);
        this.status = status;
        this.total = calculateTotal(items);
        this.createdAt = createdAt;
    }

    public static Order create(CustomerId customerId, List<OrderItem> items) {
        Objects.requireNonNull(customerId, "customerId no puede ser nulo");
        Objects.requireNonNull(items, "items no puede ser nulo");
        if (items.isEmpty()) throw new EmptyOrderException();
        return new Order(UUID.randomUUID().toString(), customerId, items, OrderStatus.PENDING, Instant.now());
    }

    public static Order reconstitute(String orderId, CustomerId customerId,
                                     List<OrderItem> items, OrderStatus status, Instant createdAt) {
        return new Order(orderId, customerId, items, status, createdAt);
    }

    public void cancel() {
        if (this.status != OrderStatus.PENDING)
            throw new OrderDomainException("ORDER_CANNOT_BE_CANCELLED",
                    "Solo se puede cancelar una orden en estado PENDING. Estado actual: " + status);
        this.status = OrderStatus.CANCELLED;
    }

    private static Money calculateTotal(List<OrderItem> items) {
        return items.stream().map(OrderItem::subtotal).reduce(Money.ZERO, Money::add);
    }

    public String getOrderId() {
        return orderId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}