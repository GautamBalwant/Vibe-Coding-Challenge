package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BuilderControllerTest {
    private MockMvc mockMvc;
    private BuilderController controller;

    @BeforeEach
    void setup() {
        controller = new BuilderController();
        var builderService = org.mockito.Mockito.mock(com.realtyx.service.BuilderService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "builderService", builderService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/builders"))
                .andExpect(status().isOk());
    }
}
