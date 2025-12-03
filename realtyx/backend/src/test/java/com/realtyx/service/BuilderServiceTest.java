package com.realtyx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuilderServiceTest {

    @Test
    void smoke() {
        BuilderService svc = new BuilderService();
        assert svc != null;
    }
}
