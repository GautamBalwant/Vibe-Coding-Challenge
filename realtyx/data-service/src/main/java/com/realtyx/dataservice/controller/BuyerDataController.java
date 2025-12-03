package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Buyer;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/buyers")
public class BuyerDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "buyers";
    private static final TypeReference<List<Buyer>> TYPE_REF = new TypeReference<>() {};

    public BuyerDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Buyer>> getAllBuyers() {
        List<Buyer> buyers = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(buyers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable long id) {
        Optional<Buyer> buyer = dataAccessService.findById(FILE_KEY, TYPE_REF, id, b -> b.id);
        return buyer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        Buyer saved = dataAccessService.save(FILE_KEY, TYPE_REF, buyer, b -> b.id, (b, id) -> b.id = id);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable long id, @RequestBody Buyer buyer) {
        buyer.id = id;
        Buyer updated = dataAccessService.save(FILE_KEY, TYPE_REF, buyer, b -> b.id, (b, newId) -> b.id = newId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable long id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, b -> b.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
