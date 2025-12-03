package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Seller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {
    private static final String DATA_FILE = "../data/sellers.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Seller> getAllSellers() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Seller>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Seller> getSellerById(Long id) {
        return getAllSellers().stream()
                .filter(seller -> seller.getId().equals(id))
                .findFirst();
    }

    public Optional<Seller> getSellerByEmail(String email) {
        return getAllSellers().stream()
                .filter(seller -> seller.getEmail().equals(email))
                .findFirst();
    }

    public Seller saveSeller(Seller seller) {
        List<Seller> sellers = getAllSellers();
        
        // Generate new ID if not present
        if (seller.getId() == null) {
            long maxId = sellers.stream()
                    .mapToLong(Seller::getId)
                    .max()
                    .orElse(0L);
            seller.setId(maxId + 1);
        }
        
        // Update existing seller or add new one
        sellers.removeIf(s -> s.getId().equals(seller.getId()));
        sellers.add(seller);
        
        saveToFile(sellers);
        return seller;
    }

    public boolean deleteSeller(Long id) {
        List<Seller> sellers = getAllSellers();
        boolean removed = sellers.removeIf(seller -> seller.getId().equals(id));
        if (removed) {
            saveToFile(sellers);
        }
        return removed;
    }

    public Seller addPropertyToSeller(Long sellerId, Long propertyId) {
        Optional<Seller> sellerOpt = getSellerById(sellerId);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (!seller.getPropertyIds().contains(propertyId)) {
                seller.getPropertyIds().add(propertyId);
                seller.setActiveListings(seller.getPropertyIds().size());
                return saveSeller(seller);
            }
        }
        return null;
    }

    private void saveToFile(List<Seller> sellers) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), sellers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
