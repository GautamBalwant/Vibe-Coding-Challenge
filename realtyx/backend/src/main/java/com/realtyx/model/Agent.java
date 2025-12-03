package com.realtyx.model;

import lombok.Data;
import java.util.List;

@Data
public class Agent {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String licenseNumber;
    private String specialization;
    private Integer yearsOfExperience;
    private Double rating;
    private Integer totalSales;
    private Integer activeListings;
    private Boolean isActive;
    private String joinedDate;
    private String profileImage;
    private String bio;
    private List<Review> reviews;
}
