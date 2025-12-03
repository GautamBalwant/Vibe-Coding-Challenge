package com.realtyx.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class Buyer {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String profileImage;
    private Boolean isActive;
    private String registeredDate;
    
    // Preferences
    private BuyerPreferences preferences;
    
    // Saved properties and searches
    private List<Long> savedProperties;
    private List<Long> viewedProperties;
    
    @Data
    public static class BuyerPreferences {
        private List<String> preferredCities;
        private List<String> propertyTypes;
        private Integer minBedrooms;
        private Integer maxBedrooms;
        private Double minPrice;
        private Double maxPrice;
        private Integer minArea;
        private Integer maxArea;
        private List<String> mustHaveAmenities;
        private String moveInDate;
    }
}
