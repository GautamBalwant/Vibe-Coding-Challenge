package com.realtyx.model;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private Long reviewerId; // Buyer or Seller ID
    private String reviewerName;
    private String reviewerType; // "buyer" or "seller"
    private Long targetId; // Agent or Builder ID
    private String targetType; // "agent" or "builder"
    private Integer rating; // 1-5 stars
    private String title;
    private String comment;
    private String date;
    private Boolean isVerified;
}
