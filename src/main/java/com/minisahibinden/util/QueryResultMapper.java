package com.minisahibinden.util;

import com.minisahibinden.dto.CategoryStatisticsDTO;
import com.minisahibinden.dto.ListingDetailDTO;
import com.minisahibinden.dto.UserListingStatsDTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to map Object[] results from native SQL queries to DTOs
 */
public class QueryResultMapper {

    /**
     * Maps Object[] array to CategoryStatisticsDTO
     * Expected order: categoryId, categoryName, listingCount, averagePrice, minPrice, maxPrice, totalValue
     */
    public static CategoryStatisticsDTO mapToCategoryStatistics(Object[] row) {
        if (row == null || row.length < 7) {
            return null;
        }
        
        CategoryStatisticsDTO dto = new CategoryStatisticsDTO();
        dto.setCategoryId(((Number) row[0]).longValue());
        dto.setCategoryName((String) row[1]);
        dto.setListingCount(((Number) row[2]).longValue());
        dto.setAveragePrice(convertToBigDecimal(row[3]));
        dto.setMinPrice(convertToBigDecimal(row[4]));
        dto.setMaxPrice(convertToBigDecimal(row[5]));
        dto.setTotalValue(convertToBigDecimal(row[6]));
        
        return dto;
    }

    /**
     * Maps Object[] array to ListingDetailDTO
     * Expected order: listingId, title, description, price, datePosted, userId, 
     *                 userFirstName, userLastName, userEmail, categoryId, categoryName
     */
    public static ListingDetailDTO mapToListingDetail(Object[] row) {
        if (row == null || row.length < 11) {
            return null;
        }
        
        ListingDetailDTO dto = new ListingDetailDTO();
        dto.setListingId(((Number) row[0]).longValue());
        dto.setTitle((String) row[1]);
        dto.setDescription((String) row[2]);
        dto.setPrice(convertToBigDecimal(row[3]));
        
        // Handle date conversion
        if (row[4] instanceof Date) {
            dto.setDatePosted(((Date) row[4]).toLocalDate());
        } else if (row[4] instanceof LocalDate) {
            dto.setDatePosted((LocalDate) row[4]);
        }
        
        dto.setUserId(((Number) row[5]).longValue());
        dto.setUserFirstName((String) row[6]);
        dto.setUserLastName((String) row[7]);
        dto.setUserEmail((String) row[8]);
        dto.setCategoryId(((Number) row[9]).longValue());
        dto.setCategoryName((String) row[10]);
        
        return dto;
    }

    /**
     * Maps Object[] array to UserListingStatsDTO
     * Expected order: userId, firstName, lastName, email, listingCount, totalListingValue, averageListingPrice
     */
    public static UserListingStatsDTO mapToUserListingStats(Object[] row) {
        if (row == null || row.length < 7) {
            return null;
        }
        
        UserListingStatsDTO dto = new UserListingStatsDTO();
        dto.setUserId(((Number) row[0]).longValue());
        dto.setFirstName((String) row[1]);
        dto.setLastName((String) row[2]);
        dto.setEmail((String) row[3]);
        dto.setListingCount(((Number) row[4]).longValue());
        dto.setTotalListingValue(convertToBigDecimal(row[5]));
        dto.setAverageListingPrice(convertToBigDecimal(row[6]));
        
        return dto;
    }

    /**
     * Maps Object[] array for top listings by category query
     * Expected order: listingId, title, price, datePosted, categoryName, 
     *                 userFirstName, userLastName, userEmail, rankInCategory
     */
    public static ListingDetailDTO mapToTopListingByCategory(Object[] row) {
        if (row == null || row.length < 9) {
            return null;
        }
        
        ListingDetailDTO dto = new ListingDetailDTO();
        dto.setListingId(((Number) row[0]).longValue());
        dto.setTitle((String) row[1]);
        dto.setPrice(convertToBigDecimal(row[2]));
        
        // Handle date conversion
        if (row[3] instanceof Date) {
            dto.setDatePosted(((Date) row[3]).toLocalDate());
        } else if (row[3] instanceof LocalDate) {
            dto.setDatePosted((LocalDate) row[3]);
        }
        
        dto.setCategoryName((String) row[4]);
        dto.setUserFirstName((String) row[5]);
        dto.setUserLastName((String) row[6]);
        dto.setUserEmail((String) row[7]);
        // rankInCategory is at index 8, but not stored in DTO
        
        return dto;
    }

    /**
     * Helper method to convert various numeric types to BigDecimal
     */
    private static BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Batch mapping methods
     */
    public static List<CategoryStatisticsDTO> mapToCategoryStatisticsList(List<Object[]> rows) {
        List<CategoryStatisticsDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            CategoryStatisticsDTO dto = mapToCategoryStatistics(row);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    public static List<ListingDetailDTO> mapToListingDetailList(List<Object[]> rows) {
        List<ListingDetailDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            ListingDetailDTO dto = mapToListingDetail(row);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    public static List<UserListingStatsDTO> mapToUserListingStatsList(List<Object[]> rows) {
        List<UserListingStatsDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            UserListingStatsDTO dto = mapToUserListingStats(row);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }
}



