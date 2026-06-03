package com.bancolombia.orders.application.port.output;

import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);

    Optional<Order> findById(String orderId);

    List<Order> findByCustomerId(CustomerId customerId);
}