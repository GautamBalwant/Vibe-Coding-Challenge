package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Property;
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
class PropertyDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private PropertyDataController controller;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.id = 1L;
        testProperty.title = "Test Property";
        testProperty.featured = true;
        testProperty.status = "available";
    }

    @Test
    void getAllProperties_returnsListOfProperties() {
        List<Property> properties = Arrays.asList(testProperty);
        when(dataAccessService.findAll(eq("properties"), any(TypeReference.class)))
                .thenReturn(properties);

        ResponseEntity<List<Property>> response = controller.getAllProperties();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(dataAccessService).findAll(eq("properties"), any(TypeReference.class));
    }

    @Test
    void getPropertyById_whenFound_returnsProperty() {
        when(dataAccessService.findById(eq("properties"), any(TypeReference.class), eq(1L), any()))
                .thenReturn(Optional.of(testProperty));

        ResponseEntity<Property> response = controller.getPropertyById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id);
        verify(dataAccessService).findById(eq("properties"), any(TypeReference.class), eq(1L), any());
    }

    @Test
    void getPropertyById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("properties"), any(TypeReference.class), eq(999L), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Property> response = controller.getPropertyById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dataAccessService).findById(eq("properties"), any(TypeReference.class), eq(999L), any());
    }

    @Test
    void getFeaturedProperties_returnsFilteredList() {
        List<Property> properties = Arrays.asList(testProperty);
        when(dataAccessService.findByPredicate(eq("properties"), any(TypeReference.class), any()))
                .thenReturn(properties);

        ResponseEntity<List<Property>> response = controller.getFeaturedProperties();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(dataAccessService).findByPredicate(eq("properties"), any(TypeReference.class), any());
    }
}
