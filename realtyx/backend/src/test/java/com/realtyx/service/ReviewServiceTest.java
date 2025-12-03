package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Test
    void smoke() {
        ReviewService svc = new ReviewService();
        assert svc != null;
    }
}
