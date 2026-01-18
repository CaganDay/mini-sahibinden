package com.minisahibinden.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    // ---------------------------------------------------------
    // BASIC QUERIES
    // ---------------------------------------------------------
    
    // Find houses by city
    @Query(value = "SELECT * FROM house WHERE city = :city ORDER BY price DESC", nativeQuery = true)
    List<House> findByCity(@Param("city") String city);
    
    // Find houses by room count
    @Query(value = "SELECT * FROM house WHERE room_count = :roomCount ORDER BY price ASC", nativeQuery = true)
    List<House> findByRoomCount(@Param("roomCount") String roomCount);
    
    // Get distinct cities
    @Query(value = "SELECT DISTINCT city FROM house ORDER BY city ASC", nativeQuery = true)
    List<String> getDistinctCities();
    
    // Get distinct room counts
    @Query(value = "SELECT DISTINCT room_count FROM house ORDER BY room_count ASC", nativeQuery = true)
    List<String> getDistinctRoomCounts();
    
    // Search by keyword (city, district, or neighborhood)
    @Query(value = "SELECT * FROM house WHERE " +
            "city LIKE %:keyword% OR district LIKE %:keyword% OR neighborhood LIKE %:keyword% " +
            "ORDER BY price DESC", nativeQuery = true)
    List<House> searchByKeyword(@Param("keyword") String keyword);

    // ---------------------------------------------------------
    // COMPLEX QUERY 1: City Statistics
    // ---------------------------------------------------------
    // Returns statistics for each city including count, average, min, max prices
    @Query(value = "SELECT " +
            "city, " +
            "COUNT(*) AS listingCount, " +
            "AVG(price) AS avgPrice, " +
            "MIN(price) AS minPrice, " +
            "MAX(price) AS maxPrice, " +
            "AVG(square_meters) AS avgSquareMeters " +
            "FROM house " +
            "GROUP BY city " +
            "HAVING COUNT(*) >= 1 " +
            "ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<Object[]> getCityStatistics();

    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Houses with Full User Details
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "h.id, h.seller_type, h.square_meters, h.room_count, " +
            "h.city, h.district, h.neighborhood, h.date_posted, h.price, " +
            "u.id AS userId, u.first_name, u.last_name, u.email " +
            "FROM house h " +
            "LEFT JOIN users u ON h.user_id = u.id " +
            "ORDER BY h.price DESC", nativeQuery = true)
    List<Object[]> getHousesWithFullDetails();

    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Price per Square Meter Analysis
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "city, " +
            "room_count, " +
            "COUNT(*) AS listingCount, " +
            "AVG(price / square_meters) AS avgPricePerSqm, " +
            "AVG(price) AS avgPrice, " +
            "AVG(square_meters) AS avgSqm " +
            "FROM house " +
            "WHERE square_meters > 0 " +
            "GROUP BY city, room_count " +
            "ORDER BY AVG(price / square_meters) DESC", nativeQuery = true)
    List<Object[]> getPricePerSquareMeterAnalysis();

    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Filter Houses by Multiple Criteria
    // ---------------------------------------------------------
    @Query(value = "SELECT * FROM house WHERE " +
            "(:minPrice IS NULL OR price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR price <= :maxPrice) AND " +
            "(:minSqm IS NULL OR square_meters >= :minSqm) AND " +
            "(:maxSqm IS NULL OR square_meters <= :maxSqm) AND " +
            "(:city IS NULL OR city = :city) AND " +
            "(:roomCount IS NULL OR room_count = :roomCount) " +
            "ORDER BY price DESC", nativeQuery = true)
    List<House> findHousesByComplexFilter(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minSqm") Double minSqm,
            @Param("maxSqm") Double maxSqm,
            @Param("city") String city,
            @Param("roomCount") String roomCount);

    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Seller Type Statistics
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "seller_type, " +
            "COUNT(*) AS listingCount, " +
            "AVG(price) AS avgPrice, " +
            "AVG(square_meters) AS avgSquareMeters, " +
            "AVG(price / square_meters) AS avgPricePerSqm " +
            "FROM house " +
            "WHERE square_meters > 0 " +
            "GROUP BY seller_type " +
            "ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<Object[]> getSellerTypeStatistics();

    // ---------------------------------------------------------
    // COMPLEX QUERY 6: Top Districts by Listing Count
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "city, " +
            "district, " +
            "COUNT(*) AS listingCount, " +
            "AVG(price) AS avgPrice, " +
            "MIN(price) AS minPrice, " +
            "MAX(price) AS maxPrice " +
            "FROM house " +
            "GROUP BY city, district " +
            "HAVING COUNT(*) >= 1 " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 20", nativeQuery = true)
    List<Object[]> getTopDistricts();

    // ---------------------------------------------------------
    // COMPLEX QUERY 7: Price Range Distribution
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "CASE " +
            "  WHEN price < 1000000 THEN 'Under 1M' " +
            "  WHEN price BETWEEN 1000000 AND 2000000 THEN '1M - 2M' " +
            "  WHEN price BETWEEN 2000001 AND 3000000 THEN '2M - 3M' " +
            "  WHEN price BETWEEN 3000001 AND 5000000 THEN '3M - 5M' " +
            "  WHEN price BETWEEN 5000001 AND 10000000 THEN '5M - 10M' " +
            "  ELSE 'Over 10M' " +
            "END AS priceRange, " +
            "COUNT(*) AS listingCount, " +
            "AVG(square_meters) AS avgSqm " +
            "FROM house " +
            "GROUP BY priceRange " +
            "ORDER BY MIN(price)", nativeQuery = true)
    List<Object[]> getPriceRangeDistribution();

    // ---------------------------------------------------------
    // COMPLEX QUERY 8: Room Count Statistics
    // ---------------------------------------------------------
    @Query(value = "SELECT " +
            "room_count, " +
            "COUNT(*) AS listingCount, " +
            "AVG(price) AS avgPrice, " +
            "AVG(square_meters) AS avgSquareMeters, " +
            "MIN(price) AS minPrice, " +
            "MAX(price) AS maxPrice " +
            "FROM house " +
            "GROUP BY room_count " +
            "ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<Object[]> getRoomCountStatistics();
}
