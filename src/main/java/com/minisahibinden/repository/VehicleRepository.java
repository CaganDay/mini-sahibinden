package com.minisahibinden.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.minisahibinden.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    // Get all vehicles with listing info (active only) - with pagination
    @Query(value = "SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "ORDER BY l.listingDate DESC",
            countQuery = "SELECT COUNT(v) FROM Vehicle v JOIN v.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active")
    Page<Vehicle> findAllActivePaged(Pageable pageable);

    // Get all vehicles with listing info (active only)
    @Query("SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "ORDER BY l.listingDate DESC")
    List<Vehicle> findAllActive();

    // Search vehicles by model name - with pagination
    @Query(value = "SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND v.modelName LIKE %:keyword% " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(v) FROM Vehicle v JOIN v.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active AND v.modelName LIKE %:keyword%")
    Page<Vehicle> searchByModelNamePaged(@Param("keyword") String keyword, Pageable pageable);

    // Search vehicles by model name
    @Query("SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND v.modelName LIKE %:keyword% " +
            "ORDER BY l.price DESC")
    List<Vehicle> searchByModelName(@Param("keyword") String keyword);

    // Filter by year - with pagination
    @Query(value = "SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND v.modelYear = :year " +
            "ORDER BY l.price DESC",
            countQuery = "SELECT COUNT(v) FROM Vehicle v JOIN v.listing l WHERE l.status = com.minisahibinden.entity.Listing$Status.Active AND v.modelYear = :year")
    Page<Vehicle> filterByYearPaged(@Param("year") Integer year, Pageable pageable);

    // Filter by year
    @Query("SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND v.modelYear = :year " +
            "ORDER BY l.price DESC")
    List<Vehicle> filterByYear(@Param("year") Integer year);

    // Get distinct years
    @Query(value = "SELECT DISTINCT v.model_year FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "ORDER BY v.model_year DESC", nativeQuery = true)
    List<Integer> getDistinctYears();

    // Get vehicle count
    @Query(value = "SELECT COUNT(*) FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active'", nativeQuery = true)
    long countActive();

    // =====================================================
    // COMPLEX QUERIES
    // =====================================================

    // COMPLEX QUERY 1: Vehicles from active sellers (users with multiple listings)
    @Query(value = "SELECT v.* FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "INNER JOIN Users u ON l.user_id = u.user_id " +
            "WHERE l.status = 'Active' AND u.user_id IN (" +
            "  SELECT user_id FROM Listings WHERE status = 'Active' GROUP BY user_id HAVING COUNT(*) >= :minListingCount" +
            ") ORDER BY l.price DESC", nativeQuery = true)
    List<Vehicle> findVehiclesFromActiveUsersSQL(@Param("minListingCount") int minListingCount);

    // COMPLEX QUERY 2: Vehicles above average price
    @Query(value = "SELECT v.* FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' AND l.price > (" +
            "  SELECT AVG(price) FROM Listings WHERE category = 'Vehicle' AND status = 'Active'" +
            ") ORDER BY l.price DESC", nativeQuery = true)
    List<Vehicle> findVehiclesAboveAveragePriceSQL();

    // COMPLEX QUERY 3: Vehicles by price range and user name pattern
    @Query(value = "SELECT v.* FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "INNER JOIN Users u ON l.user_id = u.user_id " +
            "WHERE l.status = 'Active' " +
            "AND l.price BETWEEN :minPrice AND :maxPrice " +
            "AND u.full_name LIKE CONCAT('%', :userNamePattern, '%') " +
            "ORDER BY l.price DESC", nativeQuery = true)
    List<Vehicle> findVehiclesByPriceAndUserSQL(@Param("minPrice") java.math.BigDecimal minPrice,
                                                 @Param("maxPrice") java.math.BigDecimal maxPrice,
                                                 @Param("userNamePattern") String userNamePattern);

    // COMPLEX QUERY 4: Top N vehicles per year by price
    @Query(value = "SELECT v.* FROM (" +
            "  SELECT v.*, l.price, l.status, " +
            "  ROW_NUMBER() OVER (PARTITION BY v.model_year ORDER BY l.price DESC) as rn " +
            "  FROM Vehicles v " +
            "  INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "  WHERE l.status = 'Active'" +
            ") ranked " +
            "INNER JOIN Vehicles v ON ranked.listing_id = v.listing_id " +
            "WHERE ranked.rn <= :topN " +
            "ORDER BY ranked.model_year DESC, ranked.price DESC", nativeQuery = true)
    List<Vehicle> findTopVehiclesPerYearSQL(@Param("topN") int topN);

    // COMPLEX QUERY 5: Complex multi-condition filter with sorting
    @Query(value = "SELECT v.* FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "AND (:minYear IS NULL OR v.model_year >= :minYear) " +
            "AND (:maxYear IS NULL OR v.model_year <= :maxYear) " +
            "AND l.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:maxKilometers IS NULL OR v.kilometers <= :maxKilometers) " +
            "AND (:keyword IS NULL OR :keyword = '' OR v.model_name LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY " +
            "  CASE WHEN :sortBy = 'price' THEN l.price END DESC, " +
            "  CASE WHEN :sortBy = 'year' THEN v.model_year END DESC, " +
            "  CASE WHEN :sortBy = 'km' THEN v.kilometers END ASC", nativeQuery = true)
    List<Vehicle> findVehiclesWithComplexFilterSQL(@Param("minYear") Integer minYear,
                                                    @Param("maxYear") Integer maxYear,
                                                    @Param("minPrice") java.math.BigDecimal minPrice,
                                                    @Param("maxPrice") java.math.BigDecimal maxPrice,
                                                    @Param("maxKilometers") Integer maxKilometers,
                                                    @Param("keyword") String keyword,
                                                    @Param("sortBy") String sortBy);

    // Get year statistics (count, avg, min, max price per year)
    @Query(value = "SELECT v.model_year, COUNT(*) as count, " +
            "AVG(l.price) as avg_price, MIN(l.price) as min_price, MAX(l.price) as max_price " +
            "FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "GROUP BY v.model_year " +
            "ORDER BY v.model_year DESC", nativeQuery = true)
    List<Object[]> getYearStatistics();

    // =====================================================
    // FILTER QUERIES WITH PAGINATION
    // =====================================================

    // Advanced filter with pagination - supports all filter combinations
    @Query(value = "SELECT v FROM Vehicle v " +
            "JOIN v.listing l " +
            "JOIN l.user " +
            "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
            "AND (:minYear IS NULL OR v.modelYear >= :minYear) " +
            "AND (:maxYear IS NULL OR v.modelYear <= :maxYear) " +
            "AND (:minPrice IS NULL OR l.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR l.price <= :maxPrice) " +
            "AND (:minKm IS NULL OR v.kilometers >= :minKm) " +
            "AND (:maxKm IS NULL OR v.kilometers <= :maxKm) " +
            "AND (:modelName IS NULL OR :modelName = '' OR LOWER(v.modelName) LIKE LOWER(CONCAT('%', :modelName, '%'))) " +
            "ORDER BY l.listingDate DESC",
            countQuery = "SELECT COUNT(v) FROM Vehicle v JOIN v.listing l " +
                    "WHERE l.status = com.minisahibinden.entity.Listing$Status.Active " +
                    "AND (:minYear IS NULL OR v.modelYear >= :minYear) " +
                    "AND (:maxYear IS NULL OR v.modelYear <= :maxYear) " +
                    "AND (:minPrice IS NULL OR l.price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR l.price <= :maxPrice) " +
                    "AND (:minKm IS NULL OR v.kilometers >= :minKm) " +
                    "AND (:maxKm IS NULL OR v.kilometers <= :maxKm) " +
                    "AND (:modelName IS NULL OR :modelName = '' OR LOWER(v.modelName) LIKE LOWER(CONCAT('%', :modelName, '%')))")
    Page<Vehicle> filterVehiclesPaged(
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            @Param("minKm") Integer minKm,
            @Param("maxKm") Integer maxKm,
            @Param("modelName") String modelName,
            Pageable pageable);

    // Get min and max values for filter ranges
    @Query(value = "SELECT MIN(v.model_year), MAX(v.model_year), " +
            "MIN(l.price), MAX(l.price), " +
            "MIN(v.kilometers), MAX(v.kilometers) " +
            "FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active'", nativeQuery = true)
    Object[] getFilterRanges();

    // Get distinct model names for autocomplete
    @Query(value = "SELECT DISTINCT v.model_name FROM Vehicles v " +
            "INNER JOIN Listings l ON v.listing_id = l.listing_id " +
            "WHERE l.status = 'Active' " +
            "ORDER BY v.model_name", nativeQuery = true)
    List<String> getDistinctModelNames();
}
