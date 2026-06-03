package com.bancolombia.orders.infrastructure.adapter.web.mapper;

import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Money;
import com.bancolombia.orders.domain.model.Order;
import com.bancolombia.orders.domain.model.OrderItem;
import com.bancolombia.orders.infrastructure.adapter.web.dto.*;

import java.util.List;

public class OrderWebMapper {
    public List<OrderItem> toDomainItems(List<OrderItemRequest> requests) {
        return requests.stream()
                .map(r -> new OrderItem(r.productId(), r.quantity(), Money.of(r.unitPrice())))
                .toList();
    }

    public CustomerId toDomainCustomerId(String customerId) {
        return CustomerId.of(customerId);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(), item.getQuantity(),
                        item.getUnitPrice().getAmount(), item.subtotal().getAmount()))
                .toList();
        return new OrderResponse(order.getOrderId(), order.getCustomerId().getValue(),
                order.getStatus().name(), order.getTotal().getAmount(), order.getCreatedAt(), items);
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream().map(this::toResponse).toList();
    }
}