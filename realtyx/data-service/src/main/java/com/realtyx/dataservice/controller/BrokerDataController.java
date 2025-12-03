package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Broker;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/brokers")
public class BrokerDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "brokers";
    private static final TypeReference<List<Broker>> TYPE_REF = new TypeReference<>() {};

    public BrokerDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Broker>> getAllBrokers() {
        List<Broker> brokers = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(brokers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Broker> getBrokerById(@PathVariable int id) {
        Optional<Broker> broker = dataAccessService.findById(FILE_KEY, TYPE_REF, id, b -> b.id);
        return broker.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Broker> createBroker(@RequestBody Broker broker) {
        Broker saved = dataAccessService.save(FILE_KEY, TYPE_REF, broker, b -> b.id, (b, id) -> b.id = (int) id);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Broker> updateBroker(@PathVariable int id, @RequestBody Broker broker) {
        broker.id = id;
        Broker updated = dataAccessService.save(FILE_KEY, TYPE_REF, broker, b -> b.id, (b, newId) -> b.id = (int) newId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBroker(@PathVariable int id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, b -> b.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
