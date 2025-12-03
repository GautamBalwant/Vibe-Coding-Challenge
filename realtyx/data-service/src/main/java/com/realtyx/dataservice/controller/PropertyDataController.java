package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Property;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/properties")
public class PropertyDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "properties";
    private static final TypeReference<List<Property>> TYPE_REF = new TypeReference<>() {};

    public PropertyDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable long id) {
        Optional<Property> property = dataAccessService.findById(
                FILE_KEY, TYPE_REF, id, p -> p.id
        );
        return property.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Property>> getFeaturedProperties() {
        List<Property> properties = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, p -> p.featured
        );
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Property>> getPropertiesByStatus(@PathVariable String status) {
        List<Property> properties = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, p -> status.equalsIgnoreCase(p.status)
        );
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<Property>> getPropertiesByAgent(@PathVariable Long agentId) {
        List<Property> properties = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, p -> agentId.equals(p.agentId)
        );
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Property>> getPropertiesBySeller(@PathVariable Long sellerId) {
        List<Property> properties = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, p -> sellerId.equals(p.sellerId)
        );
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/builder/{builderId}")
    public ResponseEntity<List<Property>> getPropertiesByBuilder(@PathVariable Long builderId) {
        List<Property> properties = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, p -> builderId.equals(p.builderId)
        );
        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property saved = dataAccessService.save(
                FILE_KEY, TYPE_REF, property, p -> p.id, (p, id) -> p.id = id
        );
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable long id, @RequestBody Property property) {
        property.id = id;
        Property updated = dataAccessService.save(
                FILE_KEY, TYPE_REF, property, p -> p.id, (p, newId) -> p.id = newId
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable long id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, p -> p.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
