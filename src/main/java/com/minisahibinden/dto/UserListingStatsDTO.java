package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for user car statistics query results
 */
public class UserListingStatsDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Long carCount;
    private BigDecimal totalCarValue;
    private BigDecimal averageCarPrice;

    public UserListingStatsDTO() {}

    public UserListingStatsDTO(Long userId, String firstName, String lastName, String email,
                               Long carCount, BigDecimal totalCarValue, 
                               BigDecimal averageCarPrice) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.carCount = carCount;
        this.totalCarValue = totalCarValue;
        this.averageCarPrice = averageCarPrice;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getCarCount() { return carCount; }
    public void setCarCount(Long carCount) { this.carCount = carCount; }
    public BigDecimal getTotalCarValue() { return totalCarValue; }
    public void setTotalCarValue(BigDecimal totalCarValue) { this.totalCarValue = totalCarValue; }
    public BigDecimal getAverageCarPrice() { return averageCarPrice; }
    public void setAverageCarPrice(BigDecimal averageCarPrice) { this.averageCarPrice = averageCarPrice; }
}



