package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BrokerControllerTest {
    private MockMvc mockMvc;
    private BrokerController controller;

    @BeforeEach
    void setup() {
        controller = new BrokerController();
        var brokerService = org.mockito.Mockito.mock(com.realtyx.service.BrokerService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "brokerService", brokerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/brokers"))
                .andExpect(status().isOk());
    }
}
