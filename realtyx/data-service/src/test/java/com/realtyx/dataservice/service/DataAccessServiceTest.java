package com.realtyx.dataservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.config.DataSourceConfig;
import com.realtyx.dataservice.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataAccessServiceTest {

    @Mock
    private JsonFileStore<Object> jsonFileStore;

    @Mock
    private DataSourceConfig dataSourceConfig;

    @InjectMocks
    private DataAccessService service;

    private Property testProperty;
    private List<Property> testProperties;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.id = 1L;
        testProperty.title = "Test Property";
        testProperties = Arrays.asList(testProperty);
    }

    @Test
    void findAll_returnsListFromJsonFileStore() {
        when(dataSourceConfig.getFilePath("properties")).thenReturn("data/properties.json");
        when(jsonFileStore.readList(anyString(), any(TypeReference.class)))
                .thenReturn((List) testProperties);

        List<Property> result = service.findAll("properties", new TypeReference<List<Property>>() {});

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dataSourceConfig).getFilePath("properties");
        verify(jsonFileStore).readList(anyString(), any(TypeReference.class));
    }

    @Test
    void findById_whenExists_returnsOptionalWithItem() {
        when(dataSourceConfig.getFilePath("properties")).thenReturn("data/properties.json");
        when(jsonFileStore.readList(anyString(), any(TypeReference.class)))
                .thenReturn((List) testProperties);

        Optional<Property> result = service.findById("properties", 
                new TypeReference<List<Property>>() {}, 1L, p -> p.id);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id);
    }

    @Test
    void findById_whenNotExists_returnsEmptyOptional() {
        when(dataSourceConfig.getFilePath("properties")).thenReturn("data/properties.json");
        when(jsonFileStore.readList(anyString(), any(TypeReference.class)))
                .thenReturn((List) testProperties);

        Optional<Property> result = service.findById("properties", 
                new TypeReference<List<Property>>() {}, 999L, p -> p.id);

        assertFalse(result.isPresent());
    }

    @Test
    void findByPredicate_returnsFilteredList() {
        when(dataSourceConfig.getFilePath("properties")).thenReturn("data/properties.json");
        when(jsonFileStore.readList(anyString(), any(TypeReference.class)))
                .thenReturn((List) testProperties);

        List<Property> result = service.findByPredicate("properties", 
                new TypeReference<List<Property>>() {}, p -> p.id == 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id);
    }
}
