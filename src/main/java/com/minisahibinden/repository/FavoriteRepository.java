package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Favorite.FavoriteId> {

    // Find favorites by user
    @Query(value = "SELECT * FROM Favorites WHERE user_id = :userId ORDER BY added_at DESC", nativeQuery = true)
    List<Favorite> findByUserId(@Param("userId") Integer userId);

    // Check if listing is favorited by user
    @Query(value = "SELECT COUNT(*) > 0 FROM Favorites WHERE user_id = :userId AND listing_id = :listingId", nativeQuery = true)
    boolean isFavorited(@Param("userId") Integer userId, @Param("listingId") Integer listingId);

    // Count favorites for a listing
    @Query(value = "SELECT COUNT(*) FROM Favorites WHERE listing_id = :listingId", nativeQuery = true)
    long countByListingId(@Param("listingId") Integer listingId);

    // Delete favorite
    @Query(value = "DELETE FROM Favorites WHERE user_id = :userId AND listing_id = :listingId", nativeQuery = true)
    void removeFavorite(@Param("userId") Integer userId, @Param("listingId") Integer listingId);
}
