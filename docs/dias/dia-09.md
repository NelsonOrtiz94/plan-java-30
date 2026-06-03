# Día 9 — Programación reactiva: fundamentos
## Concepto base
La programación reactiva trabaja con flujos de datos. Es como una banda transportadora inteligente: los elementos llegan cuando están listos y el sistema regula la presión para no saturarse.
## Temas
- Programación asíncrona vs no bloqueante.
- Reactive Streams: Publisher, Subscriber, Subscription, Processor.
- Backpressure: el consumidor controla la velocidad del productor.
- Project Reactor: Mono (0 o 1 elemento), Flux (0 a N elementos).
- Flujos finitos e infinitos.
- Operadores: map, flatMap, filter, switchIfEmpty, onErrorResume, retry.
- Manejo de errores reactivo: onErrorReturn, onErrorResume, doOnError.
- Diferencia entre reactive y async/await.
## Analogía
- `List<Order>` → todos los datos en memoria, síncronos.
- `Flux<Order>` → flujo que emite órdenes una a una, sin bloquear el hilo.
- Backpressure → el consumidor dice "dame solo 10 a la vez", el productor respeta.
## Actividad práctica
### Consulta reactiva de órdenes
```java
// Puerto de salida reactivo
public interface ReactiveOrderRepositoryPort {
    Flux<Order> findByCustomerId(CustomerId customerId);
    Mono<Order> findById(OrderId orderId);
}
// Caso de uso reactivo
public Flux<Order> findOrdersByCustomer(CustomerId customerId) {
    return reactiveRepository.findByCustomerId(customerId)
        .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
        .map(order -> order) // transformación
        .switchIfEmpty(Flux.error(new CustomerHasNoOrdersException(customerId)))
        .onErrorResume(CustomerHasNoOrdersException.class, e -> Flux.empty());
}
```
### Manejo de errores declarativo
```java
Mono<Order> result = repository.findById(orderId)
    .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)))
    .onErrorResume(OrderNotFoundException.class,
        ex -> Mono.error(new BusinessException("ORDER_NOT_FOUND", ex.getMessage())));
```
### Antipatrón — bloquear dentro de un flujo
```java
// MAL — bloquea el scheduler de Reactor
Flux<Order> orders = repository.findAll()
    .map(order -> {
        Order enriched = externalService.getDetails(order.getId()).block(); // NUNCA
        return enriched;
    });
// BIEN — componer con flatMap
Flux<Order> orders = repository.findAll()
    .flatMap(order -> externalService.getDetails(order.getId()));
```
## Preguntas de validación
1. ¿Qué significa backpressure?
2. ¿Por qué bloquear dentro de un flujo reactivo rompe el modelo?
3. ¿Cuál es la diferencia entre una lista y un flujo?
## Mis notas y decisiones
> Registra aquí tus aprendizajes del día.
