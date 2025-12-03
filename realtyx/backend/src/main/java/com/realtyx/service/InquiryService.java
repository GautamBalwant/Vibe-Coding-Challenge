package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.PropertyInquiry;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InquiryService {
    private static final String DATA_FILE = "../data/inquiries.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PropertyInquiry> getAllInquiries() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<PropertyInquiry>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<PropertyInquiry> getInquiryById(Long id) {
        return getAllInquiries().stream()
                .filter(inquiry -> inquiry.getId().equals(id))
                .findFirst();
    }

    public List<PropertyInquiry> getInquiriesByProperty(Long propertyId) {
        return getAllInquiries().stream()
                .filter(inquiry -> inquiry.getPropertyId().equals(propertyId))
                .toList();
    }

    public List<PropertyInquiry> getInquiriesBySeller(Long sellerId) {
        return getAllInquiries().stream()
                .filter(inquiry -> inquiry.getSellerId().equals(sellerId))
                .toList();
    }

    public List<PropertyInquiry> getInquiriesByBuyer(Long buyerId) {
        return getAllInquiries().stream()
                .filter(inquiry -> inquiry.getBuyerId().equals(buyerId))
                .toList();
    }

    public List<PropertyInquiry> getInquiriesByStatus(String status) {
        return getAllInquiries().stream()
                .filter(inquiry -> inquiry.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    public PropertyInquiry saveInquiry(PropertyInquiry inquiry) {
        List<PropertyInquiry> inquiries = getAllInquiries();
        
        // Generate new ID if not present
        if (inquiry.getId() == null) {
            long maxId = inquiries.stream()
                    .mapToLong(PropertyInquiry::getId)
                    .max()
                    .orElse(0L);
            inquiry.setId(maxId + 1);
        }
        
        // Update existing inquiry or add new one
        inquiries.removeIf(i -> i.getId().equals(inquiry.getId()));
        inquiries.add(inquiry);
        
        saveToFile(inquiries);
        return inquiry;
    }

    public PropertyInquiry respondToInquiry(Long inquiryId, String response) {
        Optional<PropertyInquiry> inquiryOpt = getInquiryById(inquiryId);
        if (inquiryOpt.isPresent()) {
            PropertyInquiry inquiry = inquiryOpt.get();
            inquiry.setSellerResponse(response);
            inquiry.setResponseDate(java.time.Instant.now().toString());
            inquiry.setStatus("responded");
            return saveInquiry(inquiry);
        }
        return null;
    }

    public boolean deleteInquiry(Long id) {
        List<PropertyInquiry> inquiries = getAllInquiries();
        boolean removed = inquiries.removeIf(inquiry -> inquiry.getId().equals(id));
        if (removed) {
            saveToFile(inquiries);
        }
        return removed;
    }

    private void saveToFile(List<PropertyInquiry> inquiries) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), inquiries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
