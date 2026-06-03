package com.bancolombia.orders.application.port.input;

import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;

import java.util.List;

public interface FindOrdersByCustomerPort {
    List<Order> findByCustomer(CustomerId customerId);
}