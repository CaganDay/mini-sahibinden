package com.minisahibinden.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate datePosted;

    // Relationship: Many Listings belong to One User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relationship: Many Listings belong to One Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Listing() {
        this.datePosted = LocalDate.now(); // Set date automatically
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDate getDatePosted() { return datePosted; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}