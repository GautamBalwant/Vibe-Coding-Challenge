package com.realtyx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Property;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Test
    void smoke() {
        // Provide a JsonFileStore with ObjectMapper to satisfy constructor
        JsonFileStore<Property> store = new JsonFileStore<>(new ObjectMapper());
        PropertyService svc = new PropertyService(store);
        assert svc != null;
    }
}
