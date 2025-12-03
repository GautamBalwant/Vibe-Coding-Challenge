package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Broker;
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
class BrokerDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private BrokerDataController controller;

    private Broker testBroker;

    @BeforeEach
    void setUp() {
        testBroker = new Broker();
        testBroker.id = 1;
        testBroker.company = "Test Broker";
    }

    @Test
    void getAllBrokers_returnsListOfBrokers() {
        List<Broker> brokers = Arrays.asList(testBroker);
        when(dataAccessService.findAll(eq("brokers"), any(TypeReference.class)))
                .thenReturn(brokers);

        ResponseEntity<List<Broker>> response = controller.getAllBrokers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBrokerById_whenFound_returnsBroker() {
        when(dataAccessService.findById(eq("brokers"), any(TypeReference.class), anyLong(), any()))
                .thenReturn(Optional.of(testBroker));

        ResponseEntity<Broker> response = controller.getBrokerById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getBrokerById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("brokers"), any(TypeReference.class), anyLong(), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Broker> response = controller.getBrokerById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
