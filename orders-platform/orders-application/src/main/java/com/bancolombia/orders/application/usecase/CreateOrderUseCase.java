package com.bancolombia.orders.application.usecase;

import com.bancolombia.orders.application.port.input.CreateOrderPort;
import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;
import com.bancolombia.orders.domain.model.OrderItem;

import java.util.List;
import java.util.Objects;

public class CreateOrderUseCase implements CreateOrderPort {
    private final OrderRepositoryPort orderRepository;

    public CreateOrderUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    @Override
    public Order createOrder(CustomerId customerId, List<OrderItem> items) {
        Objects.requireNonNull(customerId, "customerId no puede ser nulo");
        Order order = Order.create(customerId, items);
        return orderRepository.save(order);
    }
}