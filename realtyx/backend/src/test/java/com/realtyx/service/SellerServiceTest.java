package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Test
    void smoke() {
        SellerService svc = new SellerService();
        assert svc != null;
    }
}
