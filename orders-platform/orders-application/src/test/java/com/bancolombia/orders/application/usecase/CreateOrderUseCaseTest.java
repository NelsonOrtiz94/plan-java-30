package com.bancolombia.orders.application.usecase;

import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.exception.EmptyOrderException;
import com.bancolombia.orders.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOrderUseCase - caso de uso crear orden")
class CreateOrderUseCaseTest {
    @Mock
    private OrderRepositoryPort orderRepository;
    private CreateOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateOrderUseCase(orderRepository);
    }

    private OrderItem item(String pid, int qty, double price) {
        return new OrderItem(pid, qty, Money.of(price));
    }

    @Test
    @DisplayName("Crea orden exitosamente y retorna la orden guardada")
    void shouldCreateOrderSuccessfully() {
        CustomerId customerId = CustomerId.of("cust-001");
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        Order result = useCase.createOrder(customerId, List.of(item("p1", 2, 15000.0)));
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getTotal().getAmount().doubleValue()).isEqualTo(30000.0);
    }

    @Test
    @DisplayName("Guarda la orden a traves del puerto de salida")
    void shouldSaveOrderThroughRepositoryPort() {
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        useCase.createOrder(CustomerId.of("cust-001"), List.of(item("p1", 1, 10000.0)));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Lanza EmptyOrderException cuando no hay items")
    void shouldThrowWhenNoItems() {
        CustomerId customerId = CustomerId.of("cust-001");
        List<OrderItem> emptyItems = List.of();
        assertThatThrownBy(() -> useCase.createOrder(customerId, emptyItems))
                .isInstanceOf(EmptyOrderException.class);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("Lanza NullPointerException cuando customerId es nulo")
    void shouldThrowWhenCustomerIdIsNull() {
        List<OrderItem> items = List.of(item("p", 1, 1000.0));
        assertThatThrownBy(() -> useCase.createOrder(null, items))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(orderRepository);
    }
}