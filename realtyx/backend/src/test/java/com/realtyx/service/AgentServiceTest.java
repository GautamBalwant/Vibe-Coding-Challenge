package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Test
    void smoke() {
        // Simple smoke test; further tests should mock dependencies and assert behavior
        AgentService svc = new AgentService();
        assert svc != null;
    }
}
