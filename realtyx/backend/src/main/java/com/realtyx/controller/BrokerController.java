package com.realtyx.controller;

import com.realtyx.model.Broker;
import com.realtyx.service.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brokers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @GetMapping
    public ResponseEntity<List<Broker>> getAllBrokers() {
        List<Broker> brokers = brokerService.getAllBrokers();
        return ResponseEntity.ok(brokers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Broker> getBrokerById(@PathVariable Long id) {
        return brokerService.getBrokerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Broker> addBroker(@RequestBody Broker broker) {
        Broker savedBroker = brokerService.addBroker(broker);
        return ResponseEntity.ok(savedBroker);
    }
}
