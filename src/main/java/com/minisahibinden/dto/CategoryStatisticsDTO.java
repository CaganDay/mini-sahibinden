package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for category statistics query results
 */
public class CategoryStatisticsDTO {
    private Long categoryId;
    private String categoryName;
    private Long listingCount;
    private BigDecimal averagePrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal totalValue;

    public CategoryStatisticsDTO() {}

    public CategoryStatisticsDTO(Long categoryId, String categoryName, Long listingCount, 
                                 BigDecimal averagePrice, BigDecimal minPrice, 
                                 BigDecimal maxPrice, BigDecimal totalValue) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.listingCount = listingCount;
        this.averagePrice = averagePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.totalValue = totalValue;
    }

    // Getters and Setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getListingCount() { return listingCount; }
    public void setListingCount(Long listingCount) { this.listingCount = listingCount; }
    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
}



