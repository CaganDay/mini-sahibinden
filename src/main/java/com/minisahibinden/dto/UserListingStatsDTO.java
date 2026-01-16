package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for user listing statistics query results
 */
public class UserListingStatsDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Long listingCount;
    private BigDecimal totalListingValue;
    private BigDecimal averageListingPrice;

    public UserListingStatsDTO() {}

    public UserListingStatsDTO(Long userId, String firstName, String lastName, String email,
                               Long listingCount, BigDecimal totalListingValue, 
                               BigDecimal averageListingPrice) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.listingCount = listingCount;
        this.totalListingValue = totalListingValue;
        this.averageListingPrice = averageListingPrice;
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
    public Long getListingCount() { return listingCount; }
    public void setListingCount(Long listingCount) { this.listingCount = listingCount; }
    public BigDecimal getTotalListingValue() { return totalListingValue; }
    public void setTotalListingValue(BigDecimal totalListingValue) { this.totalListingValue = totalListingValue; }
    public BigDecimal getAverageListingPrice() { return averageListingPrice; }
    public void setAverageListingPrice(BigDecimal averageListingPrice) { this.averageListingPrice = averageListingPrice; }
}



