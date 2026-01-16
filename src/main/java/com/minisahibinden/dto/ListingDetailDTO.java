package com.minisahibinden.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for listing details with user and category information
 */
public class ListingDetailDTO {
    private Long listingId;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate datePosted;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Long categoryId;
    private String categoryName;

    public ListingDetailDTO() {}

    public ListingDetailDTO(Long listingId, String title, String description, BigDecimal price,
                           LocalDate datePosted, Long userId, String userFirstName, 
                           String userLastName, String userEmail, Long categoryId, String categoryName) {
        this.listingId = listingId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.datePosted = datePosted;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDate getDatePosted() { return datePosted; }
    public void setDatePosted(LocalDate datePosted) { this.datePosted = datePosted; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}



