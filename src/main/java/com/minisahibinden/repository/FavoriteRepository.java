package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.minisahibinden.entity.Favorite;
import com.minisahibinden.entity.Favorite.FavoriteId;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    // Find favorites by user
    @Query(value = "SELECT * FROM Favorites WHERE user_id = :userId ORDER BY added_at DESC", nativeQuery = true)
    List<Favorite> findByUserId(@Param("userId") Integer userId);

    // Check if listing is favorited by user
    @Query(value = "SELECT COUNT(*) > 0 FROM Favorites WHERE user_id = :userId AND listing_id = :listingId", nativeQuery = true)
    boolean isFavorited(@Param("userId") Integer userId, @Param("listingId") Integer listingId);

    // Count favorites for a listing
    @Query(value = "SELECT COUNT(*) FROM Favorites WHERE listing_id = :listingId", nativeQuery = true)
    long countByListingId(@Param("listingId") Integer listingId);

    @Query(value = "SELECT COUNT(*) FROM Favorites WHERE user_id = :userId AND listing_id = :listingId", nativeQuery = true)
    int existsFavorite(@Param("userId") Integer userId, @Param("listingId") Integer listingId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Favorites (user_id, listing_id, added_at) VALUES (:userId, :listingId, NOW())", nativeQuery = true)
    int addFavorite(@Param("userId") Integer userId, @Param("listingId") Integer listingId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Favorites WHERE user_id = :userId AND listing_id = :listingId", nativeQuery = true)
    int removeFavorite(@Param("userId") Integer userId, @Param("listingId") Integer listingId);

    @Query(value = """
            SELECT l.* FROM Listings l
            INNER JOIN Favorites f ON f.listing_id = l.listing_id
            WHERE f.user_id = :userId AND l.status <> 'Deleted'
            ORDER BY f.added_at DESC
            """, nativeQuery = true)
    List<com.minisahibinden.entity.Listing> findFavoriteListingsByUserId(@Param("userId") Integer userId);
}
