package com.realtyx.model;

import lombok.Data;

@Data
public class PropertyInquiry {
    private Long id;
    private Long propertyId;
    private Long buyerId;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private Long sellerId;
    private String message;
    private String inquiryDate;
    private String status; // "new", "read", "responded", "closed"
    private String sellerResponse;
    private String responseDate;
}
