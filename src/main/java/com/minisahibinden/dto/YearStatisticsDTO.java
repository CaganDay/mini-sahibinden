package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for year-based statistics query results
 */
public class YearStatisticsDTO {
    private Integer modelYear;
    private Long carCount;
    private BigDecimal averagePrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal totalValue;
    private BigDecimal averageKilometers;

    public YearStatisticsDTO() {}

    public YearStatisticsDTO(Integer modelYear, Long carCount, 
                             BigDecimal averagePrice, BigDecimal minPrice, 
                             BigDecimal maxPrice, BigDecimal totalValue,
                             BigDecimal averageKilometers) {
        this.modelYear = modelYear;
        this.carCount = carCount;
        this.averagePrice = averagePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.totalValue = totalValue;
        this.averageKilometers = averageKilometers;
    }

    // Getters and Setters
    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    
    public Long getCarCount() { return carCount; }
    public void setCarCount(Long carCount) { this.carCount = carCount; }
    
    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
    
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    
    public BigDecimal getAverageKilometers() { return averageKilometers; }
    public void setAverageKilometers(BigDecimal averageKilometers) { this.averageKilometers = averageKilometers; }
}
