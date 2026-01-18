package com.minisahibinden.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for house details with user information
 */
public class HouseDetailDTO {
    private Long id;
    private String sellerType;
    private Double squareMeters;
    private String roomCount;
    private String city;
    private String district;
    private String neighborhood;
    private LocalDate datePosted;
    private BigDecimal price;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    public HouseDetailDTO() {}

    public HouseDetailDTO(Long id, String sellerType, Double squareMeters, String roomCount,
                          String city, String district, String neighborhood, LocalDate datePosted,
                          BigDecimal price, Long userId, String userFirstName, 
                          String userLastName, String userEmail) {
        this.id = id;
        this.sellerType = sellerType;
        this.squareMeters = squareMeters;
        this.roomCount = roomCount;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.datePosted = datePosted;
        this.price = price;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSellerType() { return sellerType; }
    public void setSellerType(String sellerType) { this.sellerType = sellerType; }
    
    public Double getSquareMeters() { return squareMeters; }
    public void setSquareMeters(Double squareMeters) { this.squareMeters = squareMeters; }
    
    public String getRoomCount() { return roomCount; }
    public void setRoomCount(String roomCount) { this.roomCount = roomCount; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    
    public LocalDate getDatePosted() { return datePosted; }
    public void setDatePosted(LocalDate datePosted) { this.datePosted = datePosted; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    
    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
