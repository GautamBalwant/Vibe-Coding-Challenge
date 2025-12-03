package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Agent;
import com.realtyx.dataservice.service.DataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private AgentDataController controller;

    private Agent testAgent;

    @BeforeEach
    void setUp() {
        testAgent = new Agent();
        testAgent.id = 1L;
        testAgent.name = "Test Agent";
    }

    @Test
    void getAllAgents_returnsListOfAgents() {
        List<Agent> agents = Arrays.asList(testAgent);
        when(dataAccessService.findAll(eq("agents"), any(TypeReference.class)))
                .thenReturn(agents);

        ResponseEntity<List<Agent>> response = controller.getAllAgents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAgentById_whenFound_returnsAgent() {
        when(dataAccessService.findById(eq("agents"), any(TypeReference.class), eq(1L), any()))
                .thenReturn(Optional.of(testAgent));

        ResponseEntity<Agent> response = controller.getAgentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id);
    }

    @Test
    void getAgentById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("agents"), any(TypeReference.class), eq(999L), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Agent> response = controller.getAgentById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
