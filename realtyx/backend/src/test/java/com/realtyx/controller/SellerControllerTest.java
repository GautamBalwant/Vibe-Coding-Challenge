package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerControllerTest {
    private MockMvc mockMvc;
    private SellerController controller;

    @BeforeEach
    void setup() {
        controller = new SellerController();
        var sellerService = org.mockito.Mockito.mock(com.realtyx.service.SellerService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "sellerService", sellerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/sellers"))
                .andExpect(status().isOk());
    }
}
