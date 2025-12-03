package com.realtyx.controller;

import com.realtyx.model.PropertyInquiry;
import com.realtyx.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiries")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @GetMapping
    public List<PropertyInquiry> getAllInquiries() {
        return inquiryService.getAllInquiries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyInquiry> getInquiryById(@PathVariable Long id) {
        Optional<PropertyInquiry> inquiry = inquiryService.getInquiryById(id);
        return inquiry.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/property/{propertyId}")
    public List<PropertyInquiry> getInquiriesByProperty(@PathVariable Long propertyId) {
        return inquiryService.getInquiriesByProperty(propertyId);
    }

    @GetMapping("/seller/{sellerId}")
    public List<PropertyInquiry> getInquiriesBySeller(@PathVariable Long sellerId) {
        return inquiryService.getInquiriesBySeller(sellerId);
    }

    @GetMapping("/buyer/{buyerId}")
    public List<PropertyInquiry> getInquiriesByBuyer(@PathVariable Long buyerId) {
        return inquiryService.getInquiriesByBuyer(buyerId);
    }

    @GetMapping("/status/{status}")
    public List<PropertyInquiry> getInquiriesByStatus(@PathVariable String status) {
        return inquiryService.getInquiriesByStatus(status);
    }

    @PostMapping
    public PropertyInquiry createInquiry(@RequestBody PropertyInquiry inquiry) {
        return inquiryService.saveInquiry(inquiry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyInquiry> updateInquiry(@PathVariable Long id, @RequestBody PropertyInquiry inquiry) {
        inquiry.setId(id);
        return ResponseEntity.ok(inquiryService.saveInquiry(inquiry));
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<PropertyInquiry> respondToInquiry(@PathVariable Long id, @RequestBody String response) {
        PropertyInquiry inquiry = inquiryService.respondToInquiry(id, response);
        return inquiry != null ? ResponseEntity.ok(inquiry) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        boolean deleted = inquiryService.deleteInquiry(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
