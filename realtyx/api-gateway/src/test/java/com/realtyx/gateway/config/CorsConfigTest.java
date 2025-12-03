package com.realtyx.gateway.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {
    @Test
    void corsConfigCreates() {
        CorsConfig config = new CorsConfig();
        assertNotNull(config);
    }
}
