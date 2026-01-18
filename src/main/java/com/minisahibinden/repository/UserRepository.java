package com.minisahibinden.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find by email
    Optional<User> findByEmail(String email);

    // ---------------------------------------------------------
    // COMPLEX QUERY: Top Users by Total Listing Value
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "u.user_id AS userId, " +
            "u.full_name AS fullName, " +
            "u.email, " +
            "COUNT(l.listing_id) AS listingCount, " +
            "COALESCE(SUM(l.price), 0) AS totalValue, " +
            "COALESCE(AVG(l.price), 0) AS averagePrice " +
            "FROM Users u " +
            "LEFT JOIN Listings l ON u.user_id = l.user_id AND l.status = 'Active' " +
            "GROUP BY u.user_id, u.full_name, u.email " +
            "ORDER BY totalValue DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> getTopUsersByListingValue();

    // ---------------------------------------------------------
    // COMPLEX QUERY: Users with listing stats by category
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "u.user_id AS userId, " +
            "u.full_name AS fullName, " +
            "SUM(CASE WHEN l.category = 'Vehicle' THEN 1 ELSE 0 END) AS vehicleCount, " +
            "SUM(CASE WHEN l.category = 'RealEstate' THEN 1 ELSE 0 END) AS realEstateCount, " +
            "COALESCE(SUM(l.price), 0) AS totalValue " +
            "FROM Users u " +
            "LEFT JOIN Listings l ON u.user_id = l.user_id AND l.status = 'Active' " +
            "GROUP BY u.user_id, u.full_name " +
            "HAVING COUNT(l.listing_id) > 0 " +
            "ORDER BY totalValue DESC", nativeQuery = true)
    List<Object[]> getUsersWithListingStats();
}
