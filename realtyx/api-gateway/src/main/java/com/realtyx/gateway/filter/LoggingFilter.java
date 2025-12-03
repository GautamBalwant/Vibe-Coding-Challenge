package com.realtyx.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        logger.info("===== Gateway Request =====");
        logger.info("Request ID: {}", request.getId());
        logger.info("Method: {}", request.getMethod());
        logger.info("Path: {}", request.getPath());
        logger.info("Headers: {}", request.getHeaders());
        logger.info("Timestamp: {}", LocalDateTime.now());
        logger.info("===========================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("===== Gateway Response =====");
            logger.info("Status Code: {}", exchange.getResponse().getStatusCode());
            logger.info("Response Headers: {}", exchange.getResponse().getHeaders());
            logger.info("============================");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}
