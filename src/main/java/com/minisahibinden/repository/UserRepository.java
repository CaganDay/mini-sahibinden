package com.minisahibinden.repository;

import com.minisahibinden.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ---------------------------------------------------------
    // COMPLEX QUERY: Top Users by Total Listing Value
    // ---------------------------------------------------------
    // Returns users ranked by their total listing value and listing count
    // Uses JOIN with GROUP BY and aggregate functions (SUM, COUNT, AVG)
    // Includes users even if they have no listings (LEFT JOIN)
    @Query(value = "SELECT " +
            "u.id AS userId, " +
            "u.first_name AS firstName, " +
            "u.last_name AS lastName, " +
            "u.email, " +
            "COUNT(l.id) AS listingCount, " +
            "COALESCE(SUM(l.price), 0) AS totalListingValue, " +
            "COALESCE(AVG(l.price), 0) AS averageListingPrice " +
            "FROM users u " +
            "LEFT JOIN listing l ON u.id = l.user_id " +
            "GROUP BY u.id, u.first_name, u.last_name, u.email " +
            "HAVING COUNT(l.id) > 0 " +
            "ORDER BY totalListingValue DESC, listingCount DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Object[]> getTopUsersByListingValue(@Param("limit") int limit);
}