package com.minisahibinden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Vehicles")
public class Vehicle {

    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Column(name = "model_name", nullable = false, length = 150)
    private String modelName;

    @Column(name = "kilometers", nullable = false)
    private Integer kilometers;

    public Vehicle() {}

    public Vehicle(Listing listing, Integer modelYear, String modelName, Integer kilometers) {
        this.listing = listing;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.kilometers = kilometers;
    }

    // Getters and Setters
    public Integer getListingId() { return listingId; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }

    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }

    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public Integer getKilometers() { return kilometers; }
    public void setKilometers(Integer kilometers) { this.kilometers = kilometers; }
}
