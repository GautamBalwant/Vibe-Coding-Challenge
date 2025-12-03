package com.realtyx.controller;

import com.realtyx.model.Buyer;
import com.realtyx.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buyers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @GetMapping
    public List<Buyer> getAllBuyers() {
        return buyerService.getAllBuyers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long id) {
        Optional<Buyer> buyer = buyerService.getBuyerById(id);
        return buyer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Buyer createBuyer(@RequestBody Buyer buyer) {
        return buyerService.saveBuyer(buyer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long id, @RequestBody Buyer buyer) {
        buyer.setId(id);
        return ResponseEntity.ok(buyerService.saveBuyer(buyer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        boolean deleted = buyerService.deleteBuyer(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{buyerId}/save-property/{propertyId}")
    public ResponseEntity<Buyer> saveProperty(@PathVariable Long buyerId, @PathVariable Long propertyId) {
        Buyer buyer = buyerService.addSavedProperty(buyerId, propertyId);
        return buyer != null ? ResponseEntity.ok(buyer) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{buyerId}/view-property/{propertyId}")
    public ResponseEntity<Buyer> viewProperty(@PathVariable Long buyerId, @PathVariable Long propertyId) {
        Buyer buyer = buyerService.addViewedProperty(buyerId, propertyId);
        return buyer != null ? ResponseEntity.ok(buyer) : ResponseEntity.notFound().build();
    }
}
