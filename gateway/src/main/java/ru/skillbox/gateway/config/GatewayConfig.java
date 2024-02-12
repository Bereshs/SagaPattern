package ru.skillbox.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service-swagger-route", r -> r.path("/order/api/**")
                        .filters(f -> f.rewritePath("/order(?<segment>/?.*)", "$\\{segment}"))
                        .uri("lb://ORDER-SERVICE"))
                .route("payment-service-swagger-route", r -> r.path("/payment/api/**")
                        .filters(f -> f.rewritePath("/payment(?<segment>/?.*)", "$\\{segment}"))
                        .uri("lb://PAYMENT-SERVICE"))
                .route("inventory-service-swagger-route", r -> r.path("/inventory/api/**")
                        .filters(f -> f.rewritePath("/inventory(?<segment>/?.*)", "$\\{segment}"))
                        .uri("lb://INVENTORY-SERVICE"))
                .route("delivery-service-swagger-route", r -> r.path("/delivery/api/**")
                        .filters(f -> f.rewritePath("/delivery(?<segment>/?.*)", "$\\{segment}"))
                        .uri("lb://DELIVERY-SERVICE"))

                .build();
    }
}
