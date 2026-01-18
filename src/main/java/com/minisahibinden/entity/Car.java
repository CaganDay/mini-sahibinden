package com.minisahibinden.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "model")
    private String model; // e.g., "Hyundai Accent 1.5 GLS"

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "kilometers")
    private Integer kilometers;

    // Relationship: Many Cars belong to One User (seller)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Car() {}

    public Car(Integer modelYear, String model, BigDecimal price, Integer kilometers) {
        this.modelYear = modelYear;
        this.model = model;
        this.price = price;
        this.kilometers = kilometers;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getKilometers() { return kilometers; }
    public void setKilometers(Integer kilometers) { this.kilometers = kilometers; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Helper method to get formatted display title
    public String getDisplayTitle() {
        return modelYear + " " + model;
    }
}
