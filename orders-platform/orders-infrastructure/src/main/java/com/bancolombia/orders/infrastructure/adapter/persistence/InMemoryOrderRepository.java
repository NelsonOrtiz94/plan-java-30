package com.bancolombia.orders.infrastructure.adapter.persistence;

import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepositoryPort {
    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        store.put(order.getOrderId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    @Override
    public List<Order> findByCustomerId(CustomerId customerId) {
        return store.values().stream().filter(o -> o.getCustomerId().equals(customerId)).toList();
    }
}