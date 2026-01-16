package com.minisahibinden.service;

import com.minisahibinden.dto.CategoryStatisticsDTO;
import com.minisahibinden.dto.ListingDetailDTO;
import com.minisahibinden.dto.UserListingStatsDTO;
import com.minisahibinden.entity.Listing;
import com.minisahibinden.repository.ListingRepository;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.util.QueryResultMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class demonstrating usage of complex SQL queries
 */
@Service
public class ComplexQueryService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public ComplexQueryService(ListingRepository listingRepository, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    /**
     * COMPLEX QUERY 1: Get category statistics
     * Returns statistics for each category including count, average, min, max prices
     */
    public List<CategoryStatisticsDTO> getCategoryStatistics() {
        List<Object[]> results = listingRepository.getCategoryStatistics();
        return QueryResultMapper.mapToCategoryStatisticsList(results);
    }

    /**
     * COMPLEX QUERY 2: Get listings with full user and category details
     * Returns complete listing information with joined user and category data
     */
    public List<ListingDetailDTO> getListingsWithFullDetails() {
        List<Object[]> results = listingRepository.getListingsWithFullDetails();
        return QueryResultMapper.mapToListingDetailList(results);
    }

    /**
     * COMPLEX QUERY 3: Find recent listings in date and price range
     * Returns listings posted within a date range and price range, optionally filtered by keyword
     */
    public List<Listing> findRecentListingsInRange(LocalDate startDate, LocalDate endDate,
                                                   BigDecimal minPrice, BigDecimal maxPrice,
                                                   String keyword, int limit) {
        return listingRepository.findRecentListingsInRange(
                startDate, endDate, minPrice, maxPrice, keyword, limit);
    }

    /**
     * COMPLEX QUERY 4: Get categories with above-average prices
     * Returns categories where average listing price exceeds the overall average
     */
    public List<CategoryStatisticsDTO> getCategoriesWithAboveAveragePrices() {
        List<Object[]> results = listingRepository.getCategoriesWithAboveAveragePrices();
        return QueryResultMapper.mapToCategoryStatisticsList(results);
    }

    /**
     * COMPLEX QUERY 5: Get top listings by category with ranking
     * Returns top listings per category with user information and ranking
     */
    public List<ListingDetailDTO> getTopListingsByCategoryWithRanking() {
        List<Object[]> results = listingRepository.getTopListingsByCategoryWithRanking();
        return results.stream()
                .map(QueryResultMapper::mapToTopListingByCategory)
                .collect(Collectors.toList());
    }

    /**
     * COMPLEX QUERY: Get top users by total listing value
     * Returns users ranked by their total listing value and listing count
     */
    public List<UserListingStatsDTO> getTopUsersByListingValue(int limit) {
        List<Object[]> results = userRepository.getTopUsersByListingValue(limit);
        return QueryResultMapper.mapToUserListingStatsList(results);
    }
}



