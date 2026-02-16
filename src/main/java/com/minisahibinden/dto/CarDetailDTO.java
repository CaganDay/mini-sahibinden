package com.minisahibinden.dto;

import java.math.BigDecimal;

/**
 * DTO for car details with user information
 */
public class CarDetailDTO {
    private Long carId;
    private Integer modelYear;
    private String model;
    private BigDecimal price;
    private Integer kilometers;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    public CarDetailDTO() {}

    public CarDetailDTO(Long carId, Integer modelYear, String model, BigDecimal price,
                        Integer kilometers, Long userId, String userFirstName, 
                        String userLastName, String userEmail) {
        this.carId = carId;
        this.modelYear = modelYear;
        this.model = model;
        this.price = price;
        this.kilometers = kilometers;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    
    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getKilometers() { return kilometers; }
    public void setKilometers(Integer kilometers) { this.kilometers = kilometers; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    
    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    // Helper method to get display title
    public String getDisplayTitle() {
        return modelYear + " " + model;
    }
}
