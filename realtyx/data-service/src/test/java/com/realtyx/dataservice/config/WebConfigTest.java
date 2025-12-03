package com.realtyx.dataservice.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebConfigTest {
    @Test
    void webConfigCreates() {
        WebConfig config = new WebConfig();
        assertNotNull(config);
    }
}
