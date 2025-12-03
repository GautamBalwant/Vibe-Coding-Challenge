package com.realtyx.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("RealtyX API Gateway is running");
        System.out.println("Gateway: http://localhost:8765");
        System.out.println("Circuit Breaker: Resilience4j");
        System.out.println("===========================================\n");
    }
}
