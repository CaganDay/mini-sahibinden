package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for city statistics query results
 */
public class CityStatisticsDTO {
    private String city;
    private Long listingCount;
    private BigDecimal avgPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double avgSquareMeters;

    public CityStatisticsDTO() {}

    public CityStatisticsDTO(String city, Long listingCount, BigDecimal avgPrice,
                             BigDecimal minPrice, BigDecimal maxPrice, Double avgSquareMeters) {
        this.city = city;
        this.listingCount = listingCount;
        this.avgPrice = avgPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgSquareMeters = avgSquareMeters;
    }

    // Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public Long getListingCount() { return listingCount; }
    public void setListingCount(Long listingCount) { this.listingCount = listingCount; }
    
    public BigDecimal getAvgPrice() { return avgPrice; }
    public void setAvgPrice(BigDecimal avgPrice) { this.avgPrice = avgPrice; }
    
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public Double getAvgSquareMeters() { return avgSquareMeters; }
    public void setAvgSquareMeters(Double avgSquareMeters) { this.avgSquareMeters = avgSquareMeters; }
}
