package com.bancolombia.orders.infrastructure.adapter.web;

import com.bancolombia.orders.application.port.input.CreateOrderPort;
import com.bancolombia.orders.application.port.input.FindOrdersByCustomerPort;
import com.bancolombia.orders.application.port.input.FindOrdersByCustomerReactivePort;
import com.bancolombia.orders.domain.exception.EmptyOrderException;
import com.bancolombia.orders.infrastructure.adapter.web.mapper.OrderWebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(OrderWebMapper.class)
@DisplayName("OrderController - adaptador web")
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CreateOrderPort createOrderPort;
    @MockBean
    FindOrdersByCustomerPort findOrdersByCustomerPort;
    @MockBean
    FindOrdersByCustomerReactivePort findOrdersByCustomerReactivePort;

    @Test
    @DisplayName("POST /api/v1/orders retorna 400 cuando customerId esta vacio")
    void shouldReturn400WhenCustomerIdIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"\",\"items\":[{\"productId\":\"p1\",\"quantity\":1,\"unitPrice\":1000}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    @DisplayName("POST /api/v1/orders retorna 422 cuando dominio lanza EmptyOrderException")
    void shouldReturn422WhenDomainThrows() throws Exception {
        when(createOrderPort.createOrder(any(), any())).thenThrow(new EmptyOrderException());
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"cust-001\",\"items\":[{\"productId\":\"p1\",\"quantity\":1,\"unitPrice\":1000}]}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("ORDER_EMPTY"));
    }

    @Test
    @DisplayName("GET /api/v1/orders retorna 200 con lista vacia")
    void shouldReturn200WithEmptyList() throws Exception {
        when(findOrdersByCustomerPort.findByCustomer(any())).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/orders").param("customerId", "cust-001"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}