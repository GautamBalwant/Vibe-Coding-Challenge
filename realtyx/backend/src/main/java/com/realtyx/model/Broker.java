package com.realtyx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Broker {
    private Long id;
    private String name;
    private String email;
    private String phone;
    
    @JsonProperty("licenseNumber")
    private String licenseNumber;
    
    @JsonProperty("specialization")
    private String specialization;
    
    @JsonProperty("yearsOfExperience")
    private Integer yearsOfExperience;
    
    @JsonProperty("rating")
    private Double rating;
    
    @JsonProperty("totalSales")
    private Integer totalSales;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("joinedDate")
    private String joinedDate;
    
    private String profileImage;
    private String bio;
}

