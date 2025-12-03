package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrokerServiceTest {

    @Test
    void smoke() {
        BrokerService svc = new BrokerService();
        assert svc != null;
    }
}
