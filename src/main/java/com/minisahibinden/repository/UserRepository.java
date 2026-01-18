package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ---------------------------------------------------------
    // COMPLEX QUERY: Top Users by Total Car Listing Value
    // ---------------------------------------------------------
    // Returns users ranked by their total car listing value and car count
    // Uses JOIN with GROUP BY and aggregate functions (SUM, COUNT, AVG)
    @Query(value = "SELECT " +
            "u.id AS userId, " +
            "u.first_name AS firstName, " +
            "u.last_name AS lastName, " +
            "u.email, " +
            "COUNT(c.id) AS carCount, " +
            "COALESCE(SUM(c.price), 0) AS totalCarValue, " +
            "COALESCE(AVG(c.price), 0) AS averageCarPrice " +
            "FROM users u " +
            "LEFT JOIN car c ON u.id = c.user_id " +
            "GROUP BY u.id, u.first_name, u.last_name, u.email " +
            "HAVING COUNT(c.id) > 0 " +
            "ORDER BY totalCarValue DESC, carCount DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Object[]> getTopUsersByCarValue(@Param("limit") int limit);
}