package com.realtyx.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Enhanced Data Service Route with Circuit Breaker
                .route("data-service-enhanced", r -> r
                        .path("/data/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .rewritePath("/data/(?<segment>.*)", "/api/data/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("dataServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/data-service"))
                                .addResponseHeader("X-Gateway-Service", "Data-Service")
                                .addResponseHeader("X-Circuit-Breaker", "Resilience4j"))
                        .uri("lb://data-service"))

                // Enhanced Backend Service Route with Circuit Breaker
                .route("backend-service-enhanced", r -> r
                        .path("/api/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("backendServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/backend-service"))
                                .addResponseHeader("X-Gateway-Service", "Backend-Service")
                                .addResponseHeader("X-Circuit-Breaker", "Resilience4j")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setStatuses(org.springframework.http.HttpStatus.BAD_GATEWAY,
                                                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)))
                        .uri("lb://backend-service"))

                .build();
    }
}
