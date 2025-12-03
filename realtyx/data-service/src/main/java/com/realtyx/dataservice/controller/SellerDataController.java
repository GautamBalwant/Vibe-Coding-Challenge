package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Seller;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/sellers")
public class SellerDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "sellers";
    private static final TypeReference<List<Seller>> TYPE_REF = new TypeReference<>() {};

    public SellerDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        List<Seller> sellers = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable long id) {
        Optional<Seller> seller = dataAccessService.findById(FILE_KEY, TYPE_REF, id, s -> s.id);
        return seller.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller saved = dataAccessService.save(FILE_KEY, TYPE_REF, seller, s -> s.id, (s, id) -> s.id = id);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable long id, @RequestBody Seller seller) {
        seller.id = id;
        Seller updated = dataAccessService.save(FILE_KEY, TYPE_REF, seller, s -> s.id, (s, newId) -> s.id = newId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable long id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, s -> s.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
