package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minisahibinden.entity.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    // Find all active listings
    @Query(value = "SELECT * FROM Listings WHERE status = 'Active' ORDER BY listing_date DESC", nativeQuery = true)
    List<Listing> findAllActive();

    // Find listings by category
    @Query(value = "SELECT * FROM Listings WHERE category = :category AND status = 'Active' ORDER BY listing_date DESC", nativeQuery = true)
    List<Listing> findByCategory(@Param("category") String category);

    // Find listings by user (excluding deleted)
    @Query(value = "SELECT * FROM Listings WHERE user_id = :userId AND status <> 'Deleted' ORDER BY listing_date DESC", nativeQuery = true)
    List<Listing> findByUserId(@Param("userId") Integer userId);

    // Count active listings
    @Query(value = "SELECT COUNT(*) FROM Listings WHERE status = 'Active'", nativeQuery = true)
    long countActive();

    // Count by category
    @Query(value = "SELECT COUNT(*) FROM Listings WHERE category = :category AND status = 'Active'", nativeQuery = true)
    long countByCategory(@Param("category") String category);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Listings SET status = 'Deleted' WHERE listing_id = :listingId AND user_id = :userId", nativeQuery = true)
    int softDeleteByIdAndUserId(@Param("listingId") Integer listingId, @Param("userId") Integer userId);

    @Query(value = "SELECT listing_id FROM Listings WHERE status = 'Active'", nativeQuery = true)
    List<Integer> findAllActiveListingIds();

    @Query(value = "SELECT listing_id FROM Listings WHERE status = 'Active' AND user_id <> :userId", nativeQuery = true)
    List<Integer> findAllActiveListingIdsNotOwnedBy(@Param("userId") Integer userId);
}
