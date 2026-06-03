package com.bancolombia.orders.infrastructure.config;

import com.bancolombia.orders.application.port.output.OrderRepositoryPort;
import com.bancolombia.orders.application.usecase.CreateOrderUseCase;
import com.bancolombia.orders.application.usecase.FindOrdersByCustomerUseCase;
import com.bancolombia.orders.application.usecase.FindOrdersByCustomerReactiveUseCase;
import com.bancolombia.orders.infrastructure.adapter.persistence.InMemoryOrderRepository;
import com.bancolombia.orders.infrastructure.adapter.web.mapper.OrderWebMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public OrderRepositoryPort orderRepositoryPort() {
        return new InMemoryOrderRepository();
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort repo) {
        return new CreateOrderUseCase(repo);
    }

    @Bean
    public FindOrdersByCustomerUseCase findOrdersByCustomerUseCase(OrderRepositoryPort repo) {
        return new FindOrdersByCustomerUseCase(repo);
    }

    @Bean
    public FindOrdersByCustomerReactiveUseCase findOrdersByCustomerReactiveUseCase(OrderRepositoryPort repo) {
        return new FindOrdersByCustomerReactiveUseCase(repo);
    }

    @Bean
    public OrderWebMapper orderWebMapper() {
        return new OrderWebMapper();
    }
}