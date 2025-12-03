package com.realtyx.gateway.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatewayConfigTest {
    @Test
    void gatewayConfigCreates() {
        GatewayConfig config = new GatewayConfig();
        assertNotNull(config);
    }
}
