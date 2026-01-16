package com.minisahibinden.repository;

import com.minisahibinden.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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


    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Listings from Users with Multiple Listings
    // ---------------------------------------------------------
    // Returns listings from users who have posted more than a certain number of listings
    // Uses subquery with GROUP BY and HAVING to find active users
    @Query(value = "SELECT l.* FROM listing l " +
            "WHERE l.user_id IN (" +
            "    SELECT user_id FROM listing " +
            "    GROUP BY user_id " +
            "    HAVING COUNT(*) >= :minListingCount" +
            ") " +
            "ORDER BY l.date_posted DESC, l.price DESC",
            nativeQuery = true)
    List<Listing> findListingsFromActiveUsersSQL(@Param("minListingCount") int minListingCount);


    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Listings Above Category Average Price
    // ---------------------------------------------------------
    // Returns listings where price is above the average price of their category
    // Uses correlated subquery to compare each listing's price with its category average
    @Query(value = "SELECT l.* FROM listing l " +
            "WHERE l.price > (" +
            "    SELECT AVG(price) FROM listing " +
            "    WHERE category_id = l.category_id" +
            ") " +
            "ORDER BY l.category_id, l.price DESC",
            nativeQuery = true)
    List<Listing> findListingsAboveCategoryAverageSQL();


    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Listings with Price Range and User Filter
    // ---------------------------------------------------------
    // Returns listings within price range, posted by users whose names match a pattern
    // Uses JOIN with users table and multiple WHERE conditions
    @Query(value = "SELECT l.* FROM listing l " +
            "INNER JOIN users u ON l.user_id = u.id " +
            "WHERE l.price BETWEEN :minPrice AND :maxPrice " +
            "AND (LOWER(u.first_name) LIKE LOWER(CONCAT('%', :userNamePattern, '%')) " +
            "     OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :userNamePattern, '%'))) " +
            "AND l.date_posted >= DATE_SUB(CURDATE(), INTERVAL :daysBack DAY) " +
            "ORDER BY l.price DESC, l.date_posted DESC",
            nativeQuery = true)
    List<Listing> findListingsByPriceAndUserSQL(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("userNamePattern") String userNamePattern,
            @Param("daysBack") int daysBack);


    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Top Listings Per Category Using Window Function
    // ---------------------------------------------------------
    // Returns top N listings per category based on price and date
    // Uses window function ROW_NUMBER() with PARTITION BY for ranking
    @Query(value = "SELECT * FROM (" +
            "    SELECT l.*, " +
            "           ROW_NUMBER() OVER (PARTITION BY l.category_id ORDER BY l.price DESC, l.date_posted DESC) AS rn " +
            "    FROM listing l" +
            ") ranked " +
            "WHERE ranked.rn <= :topN " +
            "ORDER BY ranked.category_id, ranked.price DESC",
            nativeQuery = true)
    List<Listing> findTopListingsPerCategorySQL(@Param("topN") int topN);


    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Listings with Complex Multi-Condition Filter
    // ---------------------------------------------------------
    // Returns listings matching multiple criteria: category, price range, date range, keyword search
    // Uses multiple JOINs, date functions, and complex WHERE conditions with OR/AND logic
    // Includes NOT IN subquery to exclude old listings and dynamic sorting
    @Query(value = "SELECT DISTINCT l.* FROM listing l " +
            "INNER JOIN category c ON l.category_id = c.id " +
            "INNER JOIN users u ON l.user_id = u.id " +
            "WHERE (:categoryId IS NULL OR l.category_id = :categoryId) " +
            "AND l.price >= :minPrice " +
            "AND l.price <= :maxPrice " +
            "AND l.date_posted BETWEEN :startDate AND :endDate " +
            "AND (" +
            "    LOWER(l.title) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) " +
            "    OR LOWER(l.description) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) " +
            "    OR LOWER(c.name) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%'))" +
            ") " +
            "AND l.id NOT IN (" +
            "    SELECT id FROM listing " +
            "    WHERE date_posted < DATE_SUB(CURDATE(), INTERVAL 365 DAY)" +
            ") " +
            "ORDER BY " +
            "    CASE WHEN :sortBy = 'price' THEN l.price END DESC, " +
            "    CASE WHEN :sortBy = 'date' THEN l.date_posted END DESC, " +
            "    CASE WHEN :sortBy = 'title' THEN l.title END ASC, " +
            "    l.date_posted DESC",
            nativeQuery = true)
    List<Listing> findListingsWithComplexFilterSQL(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy);


    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Category Statistics with Aggregations
    // ---------------------------------------------------------
    // Returns statistics for each category: count, average, min, max prices, and total value
    // Uses GROUP BY with multiple aggregate functions (COUNT, AVG, MIN, MAX, SUM)
    @Query(value = "SELECT " +
            "c.id AS categoryId, " +
            "c.name AS categoryName, " +
            "COUNT(l.id) AS listingCount, " +
            "COALESCE(AVG(l.price), 0) AS averagePrice, " +
            "COALESCE(MIN(l.price), 0) AS minPrice, " +
            "COALESCE(MAX(l.price), 0) AS maxPrice, " +
            "COALESCE(SUM(l.price), 0) AS totalValue " +
            "FROM category c " +
            "LEFT JOIN listing l ON c.id = l.category_id " +
            "GROUP BY c.id, c.name " +
            "ORDER BY totalValue DESC, listingCount DESC", 
            nativeQuery = true)
    List<Object[]> getCategoryStatistics();


    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Listings with Full User and Category Details
    // ---------------------------------------------------------
    // Returns listings with joined user and category information using multiple JOINs
    // Useful for displaying complete listing information without lazy loading
    @Query(value = "SELECT " +
            "l.id AS listingId, " +
            "l.title, " +
            "l.description, " +
            "l.price, " +
            "l.date_posted AS datePosted, " +
            "u.id AS userId, " +
            "u.first_name AS userFirstName, " +
            "u.last_name AS userLastName, " +
            "u.email AS userEmail, " +
            "c.id AS categoryId, " +
            "c.name AS categoryName " +
            "FROM listing l " +
            "INNER JOIN users u ON l.user_id = u.id " +
            "INNER JOIN category c ON l.category_id = c.id " +
            "ORDER BY l.date_posted DESC, l.price DESC",
            nativeQuery = true)
    List<Object[]> getListingsWithFullDetails();


    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Recent Listings in Date Range with Price Filter
    // ---------------------------------------------------------
    // Returns listings posted within a date range and price range
    // Uses date functions and multiple WHERE conditions with BETWEEN
    @Query(value = "SELECT * FROM listing " +
            "WHERE date_posted BETWEEN :startDate AND :endDate " +
            "AND price BETWEEN :minPrice AND :maxPrice " +
            "AND (LOWER(title) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%')) " +
            "     OR LOWER(description) LIKE LOWER(CONCAT('%', COALESCE(:keyword, ''), '%'))) " +
            "ORDER BY date_posted DESC, price DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Listing> findRecentListingsInRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("keyword") String keyword,
            @Param("limit") int limit);


    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Categories with Above-Average Prices
    // ---------------------------------------------------------
    // Returns categories where average listing price is above the overall average
    // Uses subquery in HAVING clause for comparison
    @Query(value = "SELECT " +
            "c.id AS categoryId, " +
            "c.name AS categoryName, " +
            "COUNT(l.id) AS listingCount, " +
            "AVG(l.price) AS averagePrice " +
            "FROM category c " +
            "INNER JOIN listing l ON c.id = l.category_id " +
            "GROUP BY c.id, c.name " +
            "HAVING AVG(l.price) > (SELECT AVG(price) FROM listing) " +
            "ORDER BY averagePrice DESC",
            nativeQuery = true)
    List<Object[]> getCategoriesWithAboveAveragePrices();


    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Top Listings by Category with User Information
    // ---------------------------------------------------------
    // Returns top N listings per category with user details
    // Uses window functions (ROW_NUMBER) for ranking within categories
    @Query(value = "SELECT " +
            "l.id AS listingId, " +
            "l.title, " +
            "l.price, " +
            "l.date_posted AS datePosted, " +
            "c.name AS categoryName, " +
            "u.first_name AS userFirstName, " +
            "u.last_name AS userLastName, " +
            "u.email AS userEmail, " +
            "ROW_NUMBER() OVER (PARTITION BY l.category_id ORDER BY l.price DESC, l.date_posted DESC) AS rankInCategory " +
            "FROM listing l " +
            "INNER JOIN category c ON l.category_id = c.id " +
            "INNER JOIN users u ON l.user_id = u.id " +
            "ORDER BY c.name, l.price DESC, l.date_posted DESC",
            nativeQuery = true)
    List<Object[]> getTopListingsByCategoryWithRanking();
    
}