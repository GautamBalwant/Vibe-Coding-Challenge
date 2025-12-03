package com.realtyx.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("RealtyX Service Registry (Eureka) is running");
        System.out.println("Dashboard: http://localhost:8761");
        System.out.println("===========================================\n");
    }
}
