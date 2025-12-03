package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.model.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    
    @Value("${app.dataPath:../data}")
    private String dataPath;
    
    private final JsonFileStore<Property> jsonFileStore;
    private List<Property> properties;
    
    public PropertyService(JsonFileStore<Property> jsonFileStore) {
        this.jsonFileStore = jsonFileStore;
    }
    
    @PostConstruct
    public void init() {
        loadProperties();
    }
    
    private void loadProperties() {
        String filePath = dataPath + "/properties.json";
        properties = jsonFileStore.readList(filePath, new TypeReference<List<Property>>() {});
        System.out.println("Loaded " + properties.size() + " properties from " + filePath);
    }
    
    public List<Property> getAllProperties() {
        return properties;
    }
    
    public List<Property> getFeaturedProperties() {
        return properties.stream()
                .filter(p -> p.featured)
                .collect(Collectors.toList());
    }
    
    public Optional<Property> getPropertyById(long id) {
        return properties.stream()
                .filter(p -> p.id == id)
                .findFirst();
    }
    
    public List<Property> searchProperties(String city, String propertyType, Integer minPrice, Integer maxPrice, Integer bedrooms) {
        return properties.stream()
                .filter(p -> city == null || (p.address != null && city.equalsIgnoreCase(p.address.get("city"))))
                .filter(p -> propertyType == null || propertyType.equalsIgnoreCase(p.propertyType))
                .filter(p -> minPrice == null || p.price >= minPrice)
                .filter(p -> maxPrice == null || p.price <= maxPrice)
                .filter(p -> bedrooms == null || p.bedrooms == bedrooms)
                .collect(Collectors.toList());
    }
    
    public Property addProperty(Property property) {
        properties.add(property);
        saveProperties();
        return property;
    }
    
    private void saveProperties() {
        String filePath = dataPath + "/properties.json";
        jsonFileStore.writeList(filePath, properties);
        System.out.println("Saved " + properties.size() + " properties to " + filePath);
    }
}
