package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.RealEstate;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {

    // Get all real estate with listing info (active only) - with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = 'Active' " +
            "ORDER BY l.listingDate DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = 'Active'")
    Page<RealEstate> findAllActivePaged(Pageable pageable);

    // Filter by city - with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = 'Active' " +
            "AND r.city = :city " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = 'Active' AND r.city = :city")
    Page<RealEstate> findByCityPaged(@Param("city") String city, Pageable pageable);

    // Filter by room config - with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = 'Active' " +
            "AND r.roomConfig = :roomConfig " +
            "ORDER BY l.price ASC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = 'Active' AND r.roomConfig = :roomConfig")
    Page<RealEstate> findByRoomConfigPaged(@Param("roomConfig") String roomConfig, Pageable pageable);

    // Get distinct cities
    @Query(value = "SELECT DISTINCT r.city FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "ORDER BY r.city ASC", nativeQuery = true)
    List<String> getDistinctCities();

    // Get distinct room configs
    @Query(value = "SELECT DISTINCT r.room_config FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "ORDER BY r.room_config ASC", nativeQuery = true)
    List<String> getDistinctRoomConfigs();

    // Search by location with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = 'Active' " +
            "AND (r.city LIKE %:keyword% OR r.district LIKE %:keyword% OR r.neighborhood LIKE %:keyword%) " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = 'Active' AND (r.city LIKE %:keyword% OR r.district LIKE %:keyword% OR r.neighborhood LIKE %:keyword%)")
    Page<RealEstate> searchByLocationPaged(@Param("keyword") String keyword, Pageable pageable);

    // Get real estate count
    @Query(value = "SELECT COUNT(*) FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active'", nativeQuery = true)
    long countActive();

    // =====================================================
    // FILTER QUERIES WITH PAGINATION
    // =====================================================

    // Advanced filter with pagination - supports all filter combinations
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = 'Active' " +
            "AND (:filterCity IS NULL OR :filterCity = '' OR r.city = :filterCity) " +
            "AND (:filterRoomConfig IS NULL OR :filterRoomConfig = '' OR r.roomConfig = :filterRoomConfig) " +
            "AND (:filterSellerType IS NULL OR :filterSellerType = '' OR r.sellerType = :filterSellerType) " +
            "AND (:minPriceRe IS NULL OR l.price >= :minPriceRe) " +
            "AND (:maxPriceRe IS NULL OR l.price <= :maxPriceRe) " +
            "AND (:minArea IS NULL OR r.areaSqm >= :minArea) " +
            "AND (:maxArea IS NULL OR r.areaSqm <= :maxArea) " +
            "ORDER BY l.listingDate DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l " +
                    "WHERE l.status = 'Active' " +
                    "AND (:filterCity IS NULL OR :filterCity = '' OR r.city = :filterCity) " +
                    "AND (:filterRoomConfig IS NULL OR :filterRoomConfig = '' OR r.roomConfig = :filterRoomConfig) " +
                    "AND (:filterSellerType IS NULL OR :filterSellerType = '' OR r.sellerType = :filterSellerType) " +
                    "AND (:minPriceRe IS NULL OR l.price >= :minPriceRe) " +
                    "AND (:maxPriceRe IS NULL OR l.price <= :maxPriceRe) " +
                    "AND (:minArea IS NULL OR r.areaSqm >= :minArea) " +
                    "AND (:maxArea IS NULL OR r.areaSqm <= :maxArea)")
    Page<RealEstate> filterRealEstatePaged(
            @Param("filterCity") String filterCity,
            @Param("filterRoomConfig") String filterRoomConfig,
            @Param("filterSellerType") String filterSellerType,
            @Param("minPriceRe") java.math.BigDecimal minPriceRe,
            @Param("maxPriceRe") java.math.BigDecimal maxPriceRe,
            @Param("minArea") Integer minArea,
            @Param("maxArea") Integer maxArea,
            Pageable pageable);

    // Get min and max values for filter ranges
    @Query(value = "SELECT MIN(l.price), MAX(l.price), " +
            "MIN(r.area_sqm), MAX(r.area_sqm) " +
            "FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active'", nativeQuery = true)
    Object[] getRealEstateFilterRanges();

    // Get distinct seller types
    @Query(value = "SELECT DISTINCT r.seller_type FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' AND r.seller_type IS NOT NULL " +
            "ORDER BY r.seller_type", nativeQuery = true)
    List<String> getDistinctSellerTypes();

    // Get distinct cities from database
    @Query(value = "SELECT DISTINCT r.city FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "ORDER BY r.city", nativeQuery = true)
    List<String> getDistinctCitiesFromDb();
}
