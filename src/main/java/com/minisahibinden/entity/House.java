package com.minisahibinden.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "house")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_type")
    private String sellerType; // "Sahibinden" or "Emlak-ofisinden"

    @Column(name = "square_meters")
    private Double squareMeters;

    @Column(name = "room_count")
    private String roomCount; // e.g., "3+1", "2+1"

    @Column(name = "city")
    private String city; // il

    @Column(name = "district")
    private String district; // ilce

    @Column(name = "neighborhood")
    private String neighborhood; // mahalle

    @Column(name = "date_posted")
    private LocalDate datePosted;

    @Column(name = "price")
    private BigDecimal price;

    // Relationship: Many Houses belong to One User (seller)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public House() {}

    public House(String sellerType, Double squareMeters, String roomCount, 
                 String city, String district, String neighborhood, 
                 LocalDate datePosted, BigDecimal price) {
        this.sellerType = sellerType;
        this.squareMeters = squareMeters;
        this.roomCount = roomCount;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
        this.datePosted = datePosted;
        this.price = price;
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
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
