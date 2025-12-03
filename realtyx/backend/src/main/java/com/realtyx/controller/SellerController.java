package com.realtyx.controller;

import com.realtyx.model.Seller;
import com.realtyx.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Optional<Seller> seller = sellerService.getSellerById(id);
        return seller.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Seller createSeller(@RequestBody Seller seller) {
        return sellerService.saveSeller(seller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller seller) {
        seller.setId(id);
        return ResponseEntity.ok(sellerService.saveSeller(seller));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        boolean deleted = sellerService.deleteSeller(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{sellerId}/add-property/{propertyId}")
    public ResponseEntity<Seller> addProperty(@PathVariable Long sellerId, @PathVariable Long propertyId) {
        Seller seller = sellerService.addPropertyToSeller(sellerId, propertyId);
        return seller != null ? ResponseEntity.ok(seller) : ResponseEntity.notFound().build();
    }
}
