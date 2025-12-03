package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Buyer;
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
class BuyerDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private BuyerDataController controller;

    private Buyer testBuyer;

    @BeforeEach
    void setUp() {
        testBuyer = new Buyer();
        testBuyer.id = 1L;
        testBuyer.name = "Test Buyer";
    }

    @Test
    void getAllBuyers_returnsListOfBuyers() {
        List<Buyer> buyers = Arrays.asList(testBuyer);
        when(dataAccessService.findAll(eq("buyers"), any(TypeReference.class)))
                .thenReturn(buyers);

        ResponseEntity<List<Buyer>> response = controller.getAllBuyers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBuyerById_whenFound_returnsBuyer() {
        when(dataAccessService.findById(eq("buyers"), any(TypeReference.class), eq(1L), any()))
                .thenReturn(Optional.of(testBuyer));

        ResponseEntity<Buyer> response = controller.getBuyerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getBuyerById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("buyers"), any(TypeReference.class), eq(999L), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Buyer> response = controller.getBuyerById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
