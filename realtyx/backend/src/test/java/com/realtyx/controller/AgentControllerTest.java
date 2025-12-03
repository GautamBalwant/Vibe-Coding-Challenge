package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AgentControllerTest {
    private MockMvc mockMvc;
    private AgentController controller;

    @BeforeEach
    void setup() {
        controller = new AgentController();
        var agentService = org.mockito.Mockito.mock(com.realtyx.service.AgentService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "agentService", agentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/agents"))
                .andExpect(status().isOk());
    }
}
