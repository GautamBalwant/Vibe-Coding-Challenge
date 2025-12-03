package com.realtyx.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InquiryControllerTest {
    private MockMvc mockMvc;
    private InquiryController controller;

    @BeforeEach
    void setup() {
        controller = new InquiryController();
        var inquiryService = org.mockito.Mockito.mock(com.realtyx.service.InquiryService.class);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "inquiryService", inquiryService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void smokeTest_getEndpoints() throws Exception {
        mockMvc.perform(get("/api/inquiries"))
                .andExpect(status().isOk());
    }
}
