package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    @Test
    void smoke() {
        InquiryService svc = new InquiryService();
        assert svc != null;
    }
}
