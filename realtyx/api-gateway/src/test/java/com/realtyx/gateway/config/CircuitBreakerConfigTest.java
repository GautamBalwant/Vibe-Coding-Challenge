package com.realtyx.gateway.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircuitBreakerConfigTest {
    @Test
    void circuitBreakerConfigCreates() {
        CircuitBreakerConfig config = new CircuitBreakerConfig();
        assertNotNull(config);
    }
}
