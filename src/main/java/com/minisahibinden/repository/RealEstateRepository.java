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
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "ORDER BY l.listingDate DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active")
    Page<RealEstate> findAllActivePaged(Pageable pageable);

    // Get all real estate with listing info (active only)
    @Query("SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "ORDER BY l.listingDate DESC")
    List<RealEstate> findAllActive();

    // Filter by city - with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND r.city = :city " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active AND r.city = :city")
    Page<RealEstate> findByCityPaged(@Param("city") String city, Pageable pageable);

    // Filter by city
    @Query("SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND r.city = :city " +
            "ORDER BY l.price DESC")
    List<RealEstate> findByCity(@Param("city") String city);

    // Filter by room config - with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND r.roomConfig = :roomConfig " +
            "ORDER BY l.price ASC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active AND r.roomConfig = :roomConfig")
    Page<RealEstate> findByRoomConfigPaged(@Param("roomConfig") String roomConfig, Pageable pageable);

    // Filter by room config
    @Query("SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND r.roomConfig = :roomConfig " +
            "ORDER BY l.price ASC")
    List<RealEstate> findByRoomConfig(@Param("roomConfig") String roomConfig);

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

    // Search by location (city, district, or neighborhood)
    @Query(value = "SELECT r.* FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' AND " +
            "(r.city LIKE %:keyword% OR r.district LIKE %:keyword% OR r.neighborhood LIKE %:keyword%) " +
            "ORDER BY l.price DESC", nativeQuery = true)
    List<RealEstate> searchByLocation(@Param("keyword") String keyword);

    // Search by location with pagination
    @Query(value = "SELECT r FROM RealEstate r " +
            "JOIN r.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND (r.city LIKE %:keyword% OR r.district LIKE %:keyword% OR r.neighborhood LIKE %:keyword%) " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(r) FROM RealEstate r JOIN r.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active AND (r.city LIKE %:keyword% OR r.district LIKE %:keyword% OR r.neighborhood LIKE %:keyword%)")
    Page<RealEstate> searchByLocationPaged(@Param("keyword") String keyword, Pageable pageable);

    // Get real estate count
    @Query(value = "SELECT COUNT(*) FROM RealEstate r " +
            "INNER JOIN Listings l ON r.listing_id = l.listing_id " +
            "WHERE l.status = 'Active'", nativeQuery = true)
    long countActive();
}
