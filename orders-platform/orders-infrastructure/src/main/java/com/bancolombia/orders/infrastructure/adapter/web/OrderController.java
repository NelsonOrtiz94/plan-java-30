package com.bancolombia.orders.infrastructure.adapter.web;

import com.bancolombia.orders.application.port.input.CreateOrderPort;
import com.bancolombia.orders.application.port.input.FindOrdersByCustomerPort;
import com.bancolombia.orders.application.port.input.FindOrdersByCustomerReactivePort;
import com.bancolombia.orders.domain.model.Order;
import com.bancolombia.orders.infrastructure.adapter.web.dto.*;
import com.bancolombia.orders.infrastructure.adapter.web.mapper.OrderWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "API de gestion de ordenes de compra")
public class OrderController {
    private final CreateOrderPort createOrderPort;
    private final FindOrdersByCustomerPort findOrdersByCustomerPort;
    private final FindOrdersByCustomerReactivePort findOrdersByCustomerReactivePort;
    private final OrderWebMapper mapper;

    public OrderController(CreateOrderPort createOrderPort,
                           FindOrdersByCustomerPort findOrdersByCustomerPort,
                           FindOrdersByCustomerReactivePort findOrdersByCustomerReactivePort,
                           OrderWebMapper mapper) {
        this.createOrderPort = createOrderPort;
        this.findOrdersByCustomerPort = findOrdersByCustomerPort;
        this.findOrdersByCustomerReactivePort = findOrdersByCustomerReactivePort;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva orden")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada"),
            @ApiResponse(responseCode = "400", description = "Request invalida"),
            @ApiResponse(responseCode = "422", description = "Regla de negocio violada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderPort.createOrder(
                mapper.toDomainCustomerId(request.customerId()),
                mapper.toDomainItems(request.items()));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(order));
    }

    @GetMapping
    @Operation(summary = "Consultar ordenes de un cliente (bloqueante)")
    public ResponseEntity<List<OrderResponse>> findByCustomer(@RequestParam String customerId) {
        List<Order> orders = findOrdersByCustomerPort.findByCustomer(
                mapper.toDomainCustomerId(customerId));
        return ResponseEntity.ok(mapper.toResponseList(orders));
    }

    /**
     * Día 9 — Endpoint reactivo.
     *
     * Produce APPLICATION_NDJSON (Newline-Delimited JSON): cada orden se emite
     * como un elemento JSON separado por salto de línea, en cuanto esté disponible.
     * Esto demuestra backpressure y emisión elemento a elemento (streaming HTTP).
     *
     * Diferencia con GET /api/v1/orders:
     *  - Bloqueante: espera toda la lista → serializa todo → responde
     *  - Reactivo:   emite cada Order a medida que el Flux la produce
     */
    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @Operation(summary = "Consultar ordenes de un cliente como flujo reactivo (Flux / NDJSON)")
    public Flux<OrderResponse> findByCustomerReactive(@RequestParam String customerId) {
        return findOrdersByCustomerReactivePort
                .findByCustomerReactive(mapper.toDomainCustomerId(customerId))
                .map(mapper::toResponse);
    }
}