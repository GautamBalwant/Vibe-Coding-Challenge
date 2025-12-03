package com.realtyx.dataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DataServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("RealtyX Data Service is running on port 9090");
        System.out.println("===========================================\n");
    }
}
