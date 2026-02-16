package com.minisahibinden.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "Favorites")
public class Favorite {

    @Embeddable
    public static class FavoriteId implements java.io.Serializable {
        @Column(name = "user_id")
        private Integer userId;

        @Column(name = "listing_id")
        private Integer listingId;

        public FavoriteId() {}

        public FavoriteId(Integer userId, Integer listingId) {
            this.userId = userId;
            this.listingId = listingId;
        }

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public Integer getListingId() { return listingId; }
        public void setListingId(Integer listingId) { this.listingId = listingId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FavoriteId)) return false;
            FavoriteId that = (FavoriteId) o;
            return userId.equals(that.userId) && listingId.equals(that.listingId);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(userId, listingId);
        }
    }

    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("listingId")
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public Favorite() {}

    public Favorite(User user, Listing listing) {
        this.id = new FavoriteId(user.getUserId(), listing.getListingId());
        this.user = user;
        this.listing = listing;
        this.addedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public FavoriteId getId() { return id; }
    public void setId(FavoriteId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
