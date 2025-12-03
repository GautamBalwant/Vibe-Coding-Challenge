package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Builder;
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
class BuilderDataControllerTest {

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private BuilderDataController controller;

    private Builder testBuilder;

    @BeforeEach
    void setUp() {
        testBuilder = new Builder();
        testBuilder.id = 1L;
        testBuilder.companyName = "Test Builder";
    }

    @Test
    void getAllBuilders_returnsListOfBuilders() {
        List<Builder> builders = Arrays.asList(testBuilder);
        when(dataAccessService.findAll(eq("builders"), any(TypeReference.class)))
                .thenReturn(builders);

        ResponseEntity<List<Builder>> response = controller.getAllBuilders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBuilderById_whenFound_returnsBuilder() {
        when(dataAccessService.findById(eq("builders"), any(TypeReference.class), eq(1L), any()))
                .thenReturn(Optional.of(testBuilder));

        ResponseEntity<Builder> response = controller.getBuilderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getBuilderById_whenNotFound_returnsNotFound() {
        when(dataAccessService.findById(eq("builders"), any(TypeReference.class), eq(999L), any()))
                .thenReturn(Optional.empty());

        ResponseEntity<Builder> response = controller.getBuilderById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
