package com.realtyx.model;

import lombok.Data;
import java.util.List;

@Data
public class Seller {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String profileImage;
    private Boolean isActive;
    private String registeredDate;
    
    // Seller's properties
    private List<Long> propertyIds;
    private Integer totalPropertiesSold;
    private Integer activeListings;
}
