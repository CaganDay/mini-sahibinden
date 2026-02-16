package com.minisahibinden.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Listings")
public class Listing {

    public enum Category {
        Vehicle, RealEstate
    }

    public enum Status {
        Active, Sold, Deleted
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Integer listingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "listing_date", nullable = false)
    private LocalDate listingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Active;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RealEstate realEstate;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    public Listing() {}

    public Listing(User user, BigDecimal price, LocalDate listingDate, Category category) {
        this.user = user;
        this.price = price;
        this.listingDate = listingDate;
        this.category = category;
        this.status = Status.Active;
    }

    // Getters and Setters
    public Integer getListingId() { return listingId; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDate getListingDate() { return listingDate; }
    public void setListingDate(LocalDate listingDate) { this.listingDate = listingDate; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public RealEstate getRealEstate() { return realEstate; }
    public void setRealEstate(RealEstate realEstate) { this.realEstate = realEstate; }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) { this.favorites = favorites; }

    public boolean isOwnedBy(User candidate) {
        return candidate != null
                && this.user != null
                && this.user.getUserId() != null
                && this.user.getUserId().equals(candidate.getUserId());
    }

    public String getDisplayTitle() {
        if (category == Category.Vehicle && vehicle != null) {
            return vehicle.getModelYear() + " " + vehicle.getModelName();
        }
        if (category == Category.RealEstate && realEstate != null) {
            return realEstate.getCity() + ", " + realEstate.getDistrict();
        }
        return "Listing #" + listingId;
    }
}
