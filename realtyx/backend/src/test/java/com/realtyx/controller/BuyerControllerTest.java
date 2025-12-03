package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BuyerControllerTest {
    private MockMvc mockMvc;
    private BuyerController controller;

    @BeforeEach
    void setup() {
        controller = new BuyerController();
        var buyerService = org.mockito.Mockito.mock(com.realtyx.service.BuyerService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "buyerService", buyerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk());
    }
}
