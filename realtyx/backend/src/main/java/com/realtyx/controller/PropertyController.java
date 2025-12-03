package com.realtyx.controller;

import com.realtyx.model.Property;
import com.realtyx.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PropertyController {
    
    private final PropertyService propertyService;
    
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "1000") Integer limit
    ) {
        if (featured != null && featured) {
            return ResponseEntity.ok(propertyService.getFeaturedProperties());
        }
        
        List<Property> properties;
        
        if (city != null || propertyType != null || minPrice != null || maxPrice != null || bedrooms != null) {
            properties = propertyService.searchProperties(city, propertyType, minPrice, maxPrice, bedrooms);
        } else {
            properties = propertyService.getAllProperties();
        }
        
        // Apply pagination
        int start = page * limit;
        int end = Math.min(start + limit, properties.size());
        
        if (start >= properties.size()) {
            return ResponseEntity.ok(List.of());
        }
        
        return ResponseEntity.ok(properties.subList(start, end));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable long id) {
        return propertyService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Property> addProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.addProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
