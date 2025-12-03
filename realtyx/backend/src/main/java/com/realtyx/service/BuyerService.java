package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Buyer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {
    private static final String DATA_FILE = "../data/buyers.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Buyer> getAllBuyers() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Buyer>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Buyer> getBuyerById(Long id) {
        return getAllBuyers().stream()
                .filter(buyer -> buyer.getId().equals(id))
                .findFirst();
    }

    public Optional<Buyer> getBuyerByEmail(String email) {
        return getAllBuyers().stream()
                .filter(buyer -> buyer.getEmail().equals(email))
                .findFirst();
    }

    public Buyer saveBuyer(Buyer buyer) {
        List<Buyer> buyers = getAllBuyers();
        
        // Generate new ID if not present
        if (buyer.getId() == null) {
            long maxId = buyers.stream()
                    .mapToLong(Buyer::getId)
                    .max()
                    .orElse(0L);
            buyer.setId(maxId + 1);
        }
        
        // Update existing buyer or add new one
        buyers.removeIf(b -> b.getId().equals(buyer.getId()));
        buyers.add(buyer);
        
        saveToFile(buyers);
        return buyer;
    }

    public boolean deleteBuyer(Long id) {
        List<Buyer> buyers = getAllBuyers();
        boolean removed = buyers.removeIf(buyer -> buyer.getId().equals(id));
        if (removed) {
            saveToFile(buyers);
        }
        return removed;
    }

    public Buyer addSavedProperty(Long buyerId, Long propertyId) {
        Optional<Buyer> buyerOpt = getBuyerById(buyerId);
        if (buyerOpt.isPresent()) {
            Buyer buyer = buyerOpt.get();
            if (!buyer.getSavedProperties().contains(propertyId)) {
                buyer.getSavedProperties().add(propertyId);
                return saveBuyer(buyer);
            }
        }
        return null;
    }

    public Buyer addViewedProperty(Long buyerId, Long propertyId) {
        Optional<Buyer> buyerOpt = getBuyerById(buyerId);
        if (buyerOpt.isPresent()) {
            Buyer buyer = buyerOpt.get();
            if (!buyer.getViewedProperties().contains(propertyId)) {
                buyer.getViewedProperties().add(propertyId);
                return saveBuyer(buyer);
            }
        }
        return null;
    }

    private void saveToFile(List<Buyer> buyers) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), buyers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
