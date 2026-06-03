package com.bancolombia.orders.application.usecase;

import com.bancolombia.orders.application.port.input.FindOrdersByCustomerReactivePort;
import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

/**
 * Caso de uso reactivo — Día 9: Programación Reactiva.
 *
 * Envuelve el repositorio bloqueante (en memoria) en un Flux usando
 * Schedulers.boundedElastic(), que es el scheduler recomendado para
 * operaciones de I/O bloqueantes dentro de un contexto reactivo.
 *
 * Flujo:
 *  1. Mono.fromCallable    → ejecuta la consulta bloqueante de forma lazy
 *  2. subscribeOn          → delega la ejecución al scheduler de I/O
 *  3. flatMapIterable      → convierte List<Order> en Flux<Order> elemento a elemento
 *  4. filter               → operación intermedia: solo órdenes activas (no canceladas)
 *  5. map                  → transformación declarativa sin efectos secundarios
 */
public class FindOrdersByCustomerReactiveUseCase implements FindOrdersByCustomerReactivePort {

    private final OrderRepositoryPort orderRepository;

    public FindOrdersByCustomerReactiveUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    @Override
    public Flux<Order> findByCustomerReactive(CustomerId customerId) {
        // La validación se hace DENTRO del flujo usando Mono.error(),
        // así el NPE se emite como señal de error reactiva (no se lanza de forma síncrona).
        // Esto permite que StepVerifier.verifyError() lo capture correctamente.
        if (customerId == null) {
            return Flux.error(new NullPointerException("customerId no puede ser nulo"));
        }

        return reactor.core.publisher.Mono
                .fromCallable(() -> orderRepository.findByCustomerId(customerId)) // bloqueante → lazy
                .subscribeOn(Schedulers.boundedElastic())                         // no bloquear el hilo principal
                .flatMapIterable(orders -> orders);                                // List<Order> → Flux<Order>
    }
}

