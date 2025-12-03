package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Seller;
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
class SellerDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private SellerDataController controller;

    private Seller testSeller;

    @BeforeEach
    void setUp() {
        testSeller = new Seller();
        testSeller.id = 1L;
        testSeller.name = "Test Seller";
    }

    @Test
    void getAllSellers_returnsListOfSellers() {
        List<Seller> sellers = Arrays.asList(testSeller);
        when(dataAccessService.findAll(eq("sellers"), any(TypeReference.class)))
                .thenReturn(sellers);

        ResponseEntity<List<Seller>> response = controller.getAllSellers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getSellerById_whenFound_returnsSeller() {
        when(dataAccessService.findById(eq("sellers"), any(TypeReference.class), eq(1L), any()))
                .thenReturn(Optional.of(testSeller));

        ResponseEntity<Seller> response = controller.getSellerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getSellerById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("sellers"), any(TypeReference.class), eq(999L), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Seller> response = controller.getSellerById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
