package com.bancolombia.orders.application.port.input;

import com.bancolombia.orders.domain.model.CustomerId;
import com.bancolombia.orders.domain.model.Order;
import reactor.core.publisher.Flux;

/**
 * Puerto de entrada reactivo — Día 9: Programación Reactiva.
 *
 * Devuelve un Flux<Order>: un flujo potencialmente infinito de órdenes
 * que el suscriptor consume de forma no bloqueante.
 *
 * Diferencias clave:
 *  - List<Order>  → bloqueante, todos los datos en memoria antes de responder
 *  - Flux<Order>  → no bloqueante, emite elemento a elemento con backpressure
 */
public interface FindOrdersByCustomerReactivePort {
    Flux<Order> findByCustomerReactive(CustomerId customerId);
}

