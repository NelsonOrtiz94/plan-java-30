package com.bancolombia.orders.application.port.input;

import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;
import com.bancolombia.orders.domain.model.OrderItem;

import java.util.List;

public interface CreateOrderPort {
    Order createOrder(CustomerId customerId, List<OrderItem> items);
}