package com.realtyx.dataservice.model;

import java.util.List;
import java.util.Map;

public class Property {
    public long id;
    public String title;
    public String description;
    public String propertyType;
    public double price;
    public String currency;
    public int bedrooms;
    public int bathrooms;
    public int areaSqft;
    public Map<String, String> address;
    public List<String> amenities;
    public Integer brokerId;
    public Long builderId;
    public String builderName;
    public Long agentId;
    public Long sellerId;
    public List<Media> media;
    public String status;
    public boolean featured;
    public String createdAt;
    public String updatedAt;
}
