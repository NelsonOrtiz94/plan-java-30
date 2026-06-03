package com.bancolombia.orders.application.usecase;

import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas reactivas — Día 9.
 *
 * StepVerifier es la herramienta de reactor-test para verificar
 * flujos de forma declarativa y síncrona en tests:
 *  - expectNext()    → verifica el próximo elemento emitido
 *  - expectComplete() → verifica que el flujo terminó correctamente
 *  - verifyError()   → verifica que el flujo terminó con error
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FindOrdersByCustomerReactiveUseCase - flujos reactivos")
class FindOrdersByCustomerReactiveUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    private FindOrdersByCustomerReactiveUseCase useCase;
    private CustomerId customerId;

    @BeforeEach
    void setUp() {
        useCase = new FindOrdersByCustomerReactiveUseCase(orderRepository);
        customerId = CustomerId.of("cust-001");
    }

    private OrderItem item(String pid, int qty, double price) {
        return new OrderItem(pid, qty, Money.of(price));
    }

    @Test
    @DisplayName("Emite cada orden del cliente como elemento del Flux")
    void shouldEmitOrdersAsFluxElements() {
        Order order1 = Order.create(customerId, List.of(item("p1", 1, 10000.0)));
        Order order2 = Order.create(customerId, List.of(item("p2", 2, 5000.0)));
        when(orderRepository.findByCustomerId(customerId)).thenReturn(List.of(order1, order2));

        Flux<Order> result = useCase.findByCustomerReactive(customerId);

        StepVerifier.create(result)
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Emite Flux vacio cuando el cliente no tiene ordenes")
    void shouldEmitEmptyFluxWhenNoOrders() {
        when(orderRepository.findByCustomerId(customerId)).thenReturn(List.of());

        Flux<Order> result = useCase.findByCustomerReactive(customerId);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("Propaga error cuando el repositorio falla")
    void shouldPropagateErrorWhenRepositoryFails() {
        when(orderRepository.findByCustomerId(any()))
                .thenThrow(new RuntimeException("DB no disponible"));

        Flux<Order> result = useCase.findByCustomerReactive(customerId);

        StepVerifier.create(result)
                .verifyError(RuntimeException.class);
    }

    @Test
    @DisplayName("Lanza NullPointerException cuando customerId es nulo")
    void shouldThrowWhenCustomerIdIsNull() {
        StepVerifier.create(useCase.findByCustomerReactive(null))
                .verifyError(NullPointerException.class);
    }
}

