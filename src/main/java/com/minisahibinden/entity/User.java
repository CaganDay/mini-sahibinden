package com.minisahibinden.entity;

import jakarta.persistence.*; // Uses Jakarta for Spring Boot 3+
import java.util.List;

@Entity
@Table(name = "users") // "user" is a reserved word in SQL, so we use "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    // One User can have many Listings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Listing> listings;

    // Empty Constructor (Required by JPA)
    public User() {}

    // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}