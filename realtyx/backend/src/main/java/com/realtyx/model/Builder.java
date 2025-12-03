package com.realtyx.model;

import lombok.Data;
import java.util.List;

@Data
public class Builder {
    private Long id;
    private String name;
    private String companyName;
    private String email;
    private String phone;
    private String password;
    private String registrationNumber;
    private String establishedYear;
    private Double rating;
    private String logo;
    private String website;
    private String description;
    private Boolean isActive;
    
    // Statistics
    private Integer totalProjects;
    private Integer completedProjects;
    private Integer ongoingProjects;
    private Integer upcomingProjects;
    private Integer totalUnitsSold;
    
    // Lists
    private List<BuilderProject> projects;
    private List<Review> reviews;
    
    @Data
    public static class BuilderProject {
        private Long projectId;
        private String projectName;
        private String status; // Completed, Ongoing, Upcoming
        private String location;
        private Integer totalUnits;
        private Integer soldUnits;
        private String launchDate;
        private String completionDate;
        private String imageUrl;
    }
}
