package com.minisahibinden.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // ---------------------------------------------------------
    // 1. SEARCH QUERY (Hand-written SQL)
    // ---------------------------------------------------------
    // "Select everything from the car table where the model contains the keyword"
    // LOWER(...) makes it case-insensitive (so 'bmw' finds 'BMW')
    @Query(value = "SELECT * FROM car WHERE LOWER(model) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Car> searchCarsSQL(@Param("keyword") String keyword);


    // ---------------------------------------------------------
    // 2. YEAR FILTER (Hand-written SQL)
    // ---------------------------------------------------------
    // "Select everything from the car table where model_year matches the year we sent"
    @Query(value = "SELECT * FROM car WHERE model_year = :year", nativeQuery = true)
    List<Car> filterByYearSQL(@Param("year") Integer year);


    // ---------------------------------------------------------
    // 3. PRICE RANGE FILTER
    // ---------------------------------------------------------
    @Query(value = "SELECT * FROM car WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY price ASC", nativeQuery = true)
    List<Car> filterByPriceRangeSQL(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);


    // ---------------------------------------------------------
    // 4. KILOMETERS FILTER
    // ---------------------------------------------------------
    @Query(value = "SELECT * FROM car WHERE kilometers <= :maxKm ORDER BY kilometers ASC", nativeQuery = true)
    List<Car> filterByMaxKilometersSQL(@Param("maxKm") Integer maxKm);


    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Cars from Users with Multiple Listings
    // ---------------------------------------------------------
    // Returns cars from users who have posted more than a certain number of cars
    // Uses subquery with GROUP BY and HAVING to find active users
    @Query(value = "SELECT c.* FROM car c " +
            "WHERE c.user_id IN (" +
            "    SELECT user_id FROM car " +
            "    GROUP BY user_id " +
            "    HAVING COUNT(*) >= :minCarCount" +
            ") " +
            "ORDER BY c.price DESC",
            nativeQuery = true)
    List<Car> findCarsFromActiveUsersSQL(@Param("minCarCount") int minCarCount);


    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Cars Above Average Price
    // ---------------------------------------------------------
    // Returns cars where price is above the overall average price
    @Query(value = "SELECT c.* FROM car c " +
            "WHERE c.price > (SELECT AVG(price) FROM car) " +
            "ORDER BY c.price DESC",
            nativeQuery = true)
    List<Car> findCarsAboveAveragePriceSQL();


    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Cars with Price Range and User Filter
    // ---------------------------------------------------------
    // Returns cars within price range, posted by users whose names match a pattern
    @Query(value = "SELECT c.* FROM car c " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "WHERE c.price BETWEEN :minPrice AND :maxPrice " +
            "AND (LOWER(u.first_name) LIKE LOWER(CONCAT('%', :userNamePattern, '%')) " +
            "     OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :userNamePattern, '%'))) " +
            "ORDER BY c.price DESC",
            nativeQuery = true)
    List<Car> findCarsByPriceAndUserSQL(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("userNamePattern") String userNamePattern);


    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Top Cars Per Year Using Window Function
    // ---------------------------------------------------------
    // Returns top N cars per model year based on price
    // Uses window function ROW_NUMBER() with PARTITION BY for ranking
    @Query(value = "SELECT * FROM (" +
            "    SELECT c.*, " +
            "           ROW_NUMBER() OVER (PARTITION BY c.model_year ORDER BY c.price DESC) AS rn " +
            "    FROM car c" +
            ") ranked " +
            "WHERE ranked.rn <= :topN " +
            "ORDER BY ranked.model_year DESC, ranked.price DESC",
            nativeQuery = true)
    List<Car> findTopCarsPerYearSQL(@Param("topN") int topN);


    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Cars with Complex Multi-Condition Filter
    // ---------------------------------------------------------
    // Returns cars matching multiple criteria: year range, price range, km range, keyword search
    @Query(value = "SELECT DISTINCT c.* FROM car c " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "WHERE (:minYear IS NULL OR c.model_year >= :minYear) " +
            "AND (:maxYear IS NULL OR c.model_year <= :maxYear) " +
            "AND c.price >= :minPrice " +
            "AND c.price <= :maxPrice " +
            "AND (:maxKilometers IS NULL OR c.kilometers <= :maxKilometers) " +
            "AND LOWER(c.model) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) " +
            "ORDER BY " +
            "    CASE WHEN :sortBy = 'price' THEN c.price END DESC, " +
            "    CASE WHEN :sortBy = 'year' THEN c.model_year END DESC, " +
            "    CASE WHEN :sortBy = 'km' THEN c.kilometers END ASC, " +
            "    c.price DESC",
            nativeQuery = true)
    List<Car> findCarsWithComplexFilterSQL(
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("maxKilometers") Integer maxKilometers,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy);


    // ---------------------------------------------------------
    // COMPLEX QUERY 6: Year Statistics with Aggregations
    // ---------------------------------------------------------
    // Returns statistics for each model year: count, average, min, max prices
    @Query(value = "SELECT " +
            "c.model_year AS modelYear, " +
            "COUNT(c.id) AS carCount, " +
            "COALESCE(AVG(c.price), 0) AS averagePrice, " +
            "COALESCE(MIN(c.price), 0) AS minPrice, " +
            "COALESCE(MAX(c.price), 0) AS maxPrice, " +
            "COALESCE(SUM(c.price), 0) AS totalValue, " +
            "COALESCE(AVG(c.kilometers), 0) AS averageKilometers " +
            "FROM car c " +
            "GROUP BY c.model_year " +
            "ORDER BY c.model_year DESC", 
            nativeQuery = true)
    List<Object[]> getYearStatistics();


    // ---------------------------------------------------------
    // COMPLEX QUERY 7: Cars with Full User Details
    // ---------------------------------------------------------
    // Returns cars with joined user information
    @Query(value = "SELECT " +
            "c.id AS carId, " +
            "c.model_year AS modelYear, " +
            "c.model, " +
            "c.price, " +
            "c.kilometers, " +
            "u.id AS userId, " +
            "u.first_name AS userFirstName, " +
            "u.last_name AS userLastName, " +
            "u.email AS userEmail " +
            "FROM car c " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "ORDER BY c.price DESC",
            nativeQuery = true)
    List<Object[]> getCarsWithFullDetails();


    // ---------------------------------------------------------
    // COMPLEX QUERY 8: Cars in Price Range with Pagination
    // ---------------------------------------------------------
    @Query(value = "SELECT * FROM car " +
            "WHERE price BETWEEN :minPrice AND :maxPrice " +
            "AND (:minKm IS NULL OR kilometers >= :minKm) " +
            "AND (:maxKm IS NULL OR kilometers <= :maxKm) " +
            "ORDER BY price DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Car> findCarsInRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minKm") Integer minKm,
            @Param("maxKm") Integer maxKm,
            @Param("limit") int limit);


    // ---------------------------------------------------------
    // COMPLEX QUERY 9: Years with Above-Average Prices
    // ---------------------------------------------------------
    // Returns model years where average car price is above the overall average
    @Query(value = "SELECT " +
            "c.model_year AS modelYear, " +
            "COUNT(c.id) AS carCount, " +
            "AVG(c.price) AS averagePrice " +
            "FROM car c " +
            "GROUP BY c.model_year " +
            "HAVING AVG(c.price) > (SELECT AVG(price) FROM car) " +
            "ORDER BY averagePrice DESC",
            nativeQuery = true)
    List<Object[]> getYearsWithAboveAveragePrices();


    // ---------------------------------------------------------
    // COMPLEX QUERY 10: Top Cars Per Year with User Information
    // ---------------------------------------------------------
    // Returns top cars per model year with user details
    @Query(value = "SELECT " +
            "c.id AS carId, " +
            "c.model_year AS modelYear, " +
            "c.model, " +
            "c.price, " +
            "c.kilometers, " +
            "u.first_name AS userFirstName, " +
            "u.last_name AS userLastName, " +
            "u.email AS userEmail, " +
            "ROW_NUMBER() OVER (PARTITION BY c.model_year ORDER BY c.price DESC) AS rankInYear " +
            "FROM car c " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "ORDER BY c.model_year DESC, c.price DESC",
            nativeQuery = true)
    List<Object[]> getTopCarsByYearWithRanking();


    // ---------------------------------------------------------
    // Get distinct model years for filtering
    // ---------------------------------------------------------
    @Query(value = "SELECT DISTINCT model_year FROM car ORDER BY model_year DESC", nativeQuery = true)
    List<Integer> getDistinctYears();
}
