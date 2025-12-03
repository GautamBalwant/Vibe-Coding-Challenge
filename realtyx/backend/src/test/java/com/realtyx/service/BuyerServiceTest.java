package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @Test
    void smoke() {
        BuyerService svc = new BuyerService();
        assert svc != null;
    }
}
