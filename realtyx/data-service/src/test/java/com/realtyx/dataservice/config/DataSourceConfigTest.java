package com.realtyx.dataservice.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceConfigTest {
    @Test
    void dataSourceConfigCreates() {
        DataSourceConfig config = new DataSourceConfig();
        assertNotNull(config);
    }
}
