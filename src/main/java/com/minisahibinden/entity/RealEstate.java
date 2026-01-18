package com.minisahibinden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "RealEstate")
public class RealEstate {

    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "seller_type", length = 50)
    private String sellerType;

    @Column(name = "area_sqm", nullable = false)
    private Integer areaSqm;

    @Column(name = "room_config", length = 20)
    private String roomConfig;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "neighborhood", length = 100)
    private String neighborhood;

    public RealEstate() {}

    public RealEstate(Listing listing, String sellerType, Integer areaSqm, String roomConfig,
                      String city, String district, String neighborhood) {
        this.listing = listing;
        this.sellerType = sellerType;
        this.areaSqm = areaSqm;
        this.roomConfig = roomConfig;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
    }

    // Getters and Setters
    public Integer getListingId() { return listingId; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }

    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }

    public String getSellerType() { return sellerType; }
    public void setSellerType(String sellerType) { this.sellerType = sellerType; }

    public Integer getAreaSqm() { return areaSqm; }
    public void setAreaSqm(Integer areaSqm) { this.areaSqm = areaSqm; }

    public String getRoomConfig() { return roomConfig; }
    public void setRoomConfig(String roomConfig) { this.roomConfig = roomConfig; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
}
