package com.bancolombia.orders.domain;

import com.bancolombia.orders.domain.exception.EmptyOrderException;
import com.bancolombia.orders.domain.exception.InvalidQuantityException;
import com.bancolombia.orders.domain.exception.OrderDomainException;
import com.bancolombia.orders.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Order - reglas de negocio del dominio")
class OrderTest {
    private final CustomerId customerId = CustomerId.of("cust-001");

    private OrderItem item(String pid, int qty, double price) {
        return new OrderItem(pid, qty, Money.of(price));
    }

    @Test
    @DisplayName("Lanza EmptyOrderException cuando no hay items")
    void shouldThrowWhenNoItems() {
        List<OrderItem> emptyItems = List.of();
        assertThatThrownBy(() -> Order.create(customerId, emptyItems))
                .isInstanceOf(EmptyOrderException.class);
    }

    @Test
    @DisplayName("Lanza InvalidQuantityException cuando cantidad es cero")
    void shouldThrowWhenQuantityIsZero() {
        assertThatThrownBy(() -> item("p1", 0, 10000.0))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    @DisplayName("Lanza InvalidQuantityException cuando cantidad es negativa")
    void shouldThrowWhenQuantityIsNegative() {
        assertThatThrownBy(() -> item("p1", -1, 10000.0))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    @DisplayName("Calcula total correctamente con un item")
    void shouldCalculateTotalWithOneItem() {
        Order order = Order.create(customerId, List.of(item("p1", 2, 15000.0)));
        assertThat(order.getTotal().getAmount()).isEqualByComparingTo(new BigDecimal("30000.00"));
    }

    @Test
    @DisplayName("Calcula total correctamente con multiples items")
    void shouldCalculateTotalWithMultipleItems() {
        Order order = Order.create(customerId, List.of(item("p1", 2, 15000.0), item("p2", 1, 45000.0)));
        assertThat(order.getTotal().getAmount()).isEqualByComparingTo(new BigDecimal("75000.00"));
    }

    @Test
    @DisplayName("Estado inicial debe ser PENDING")
    void shouldStartInPendingStatus() {
        Order order = Order.create(customerId, List.of(item("p1", 1, 10000.0)));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Cancela correctamente una orden PENDING")
    void shouldCancelPendingOrder() {
        Order order = Order.create(customerId, List.of(item("p1", 1, 10000.0)));
        order.cancel();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("Lanza excepcion al cancelar orden ya cancelada")
    void shouldThrowWhenCancellingAlreadyCancelled() {
        Order order = Order.create(customerId, List.of(item("p1", 1, 10000.0)));
        order.cancel();
        assertThatThrownBy(order::cancel).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("Genera orderId no nulo")
    void shouldGenerateNonNullOrderId() {
        Order order = Order.create(customerId, List.of(item("p1", 1, 5000.0)));
        assertThat(order.getOrderId()).isNotNull().isNotBlank();
    }
}