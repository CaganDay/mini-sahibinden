package com.minisahibinden.repository;

import com.minisahibinden.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    // ---------------------------------------------------------
    // 1. SEARCH QUERY (Hand-written SQL)
    // ---------------------------------------------------------
    // "Select everything from the listing table where the title contains the keyword"
    // LOWER(...) makes it case-insensitive (so 'honda' finds 'HONDA')
    @Query(value = "SELECT * FROM listing WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Listing> searchListingsSQL(@Param("keyword") String keyword);


    // ---------------------------------------------------------
    // 2. CATEGORY FILTER (Hand-written SQL)
    // ---------------------------------------------------------
    // "Select everything from the listing table where category_id matches the ID we sent"
    @Query(value = "SELECT * FROM listing WHERE category_id = :catId", nativeQuery = true)
    List<Listing> filterByCategorySQL(@Param("catId") Long catId);
    
}