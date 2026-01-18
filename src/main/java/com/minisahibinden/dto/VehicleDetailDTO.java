package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for vehicle details with user information
 */
public class VehicleDetailDTO {
    private Integer listingId;
    private Integer modelYear;
    private String modelName;
    private BigDecimal price;
    private Integer kilometers;
    private Integer userId;
    private String userFullName;
    private String userEmail;

    public VehicleDetailDTO() {}

    public VehicleDetailDTO(Integer listingId, Integer modelYear, String modelName, BigDecimal price,
                            Integer kilometers, Integer userId, String userFullName, String userEmail) {
        this.listingId = listingId;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.price = price;
        this.kilometers = kilometers;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public Integer getListingId() { return listingId; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    
    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getKilometers() { return kilometers; }
    public void setKilometers(Integer kilometers) { this.kilometers = kilometers; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
