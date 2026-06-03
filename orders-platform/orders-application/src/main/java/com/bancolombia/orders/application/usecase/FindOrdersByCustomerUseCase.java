package com.bancolombia.orders.application.usecase;

import com.bancolombia.orders.application.port.input.FindOrdersByCustomerPort;
import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;

import java.util.List;
import java.util.Objects;

public class FindOrdersByCustomerUseCase implements FindOrdersByCustomerPort {
    private final OrderRepositoryPort orderRepository;

    public FindOrdersByCustomerUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    @Override
    public List<Order> findByCustomer(CustomerId customerId) {
        Objects.requireNonNull(customerId, "customerId no puede ser nulo");
        return orderRepository.findByCustomerId(customerId);
    }
}