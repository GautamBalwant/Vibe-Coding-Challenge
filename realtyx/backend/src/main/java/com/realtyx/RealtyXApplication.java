package com.realtyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RealtyXApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealtyXApplication.class, args);
    }
}
