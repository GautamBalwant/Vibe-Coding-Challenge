package com.realtyx.gateway.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/data-service")
    public Mono<Map<String, Object>> dataServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "CIRCUIT_OPEN");
        response.put("service", "data-service");
        response.put("message", "Data Service is currently unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("fallback", true);
        return Mono.just(response);
    }

    @GetMapping("/fallback/backend-service")
    public Mono<Map<String, Object>> backendServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "CIRCUIT_OPEN");
        response.put("service", "backend-service");
        response.put("message", "Backend Service is currently unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("fallback", true);
        return Mono.just(response);
    }

    @GetMapping("/fallback/default")
    public Mono<Map<String, Object>> defaultFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "The requested service is currently unavailable. Our team has been notified.");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("fallback", true);
        response.put("supportContact", "support@realtyx.com");
        return Mono.just(response);
    }
}
