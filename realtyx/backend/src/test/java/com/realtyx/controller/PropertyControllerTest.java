package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PropertyControllerTest {
    private MockMvc mockMvc;
    private PropertyController controller;

    @BeforeEach
    void setup() {
        // Mock the PropertyService and pass it into the controller
        var propertyService = mock(com.realtyx.service.PropertyService.class);
        controller = new PropertyController(propertyService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/properties"))
                .andExpect(status().isOk());
    }
}
