package com.minisahibinden.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.minisahibinden.dto.CarDetailDTO;
import com.minisahibinden.dto.UserListingStatsDTO;
import com.minisahibinden.dto.YearStatisticsDTO;

/**
 * Utility class to map Object[] results from native SQL queries to DTOs
 */
public class QueryResultMapper {

    /**
     * Maps Object[] array to YearStatisticsDTO
     * Expected order: modelYear, carCount, averagePrice, minPrice, maxPrice, totalValue, averageKilometers
     */
    public static YearStatisticsDTO mapToYearStatistics(Object[] row) {
        if (row == null || row.length < 7) {
            return null;
        }
        
        YearStatisticsDTO dto = new YearStatisticsDTO();
        dto.setModelYear(((Number) row[0]).intValue());
        dto.setCarCount(((Number) row[1]).longValue());
        dto.setAveragePrice(convertToBigDecimal(row[2]));
        dto.setMinPrice(convertToBigDecimal(row[3]));
        dto.setMaxPrice(convertToBigDecimal(row[4]));
        dto.setTotalValue(convertToBigDecimal(row[5]));
        dto.setAverageKilometers(convertToBigDecimal(row[6]));
        
        return dto;
    }

    /**
     * Maps Object[] array to YearStatisticsDTO (simple version for above-average query)
     * Expected order: modelYear, carCount, averagePrice
     */
    public static YearStatisticsDTO mapToYearStatisticsSimple(Object[] row) {
        if (row == null || row.length < 3) {
            return null;
        }
        
        YearStatisticsDTO dto = new YearStatisticsDTO();
        dto.setModelYear(((Number) row[0]).intValue());
        dto.setCarCount(((Number) row[1]).longValue());
        dto.setAveragePrice(convertToBigDecimal(row[2]));
        
        return dto;
    }

    /**
     * Maps Object[] array to CarDetailDTO
     * Expected order: carId, modelYear, model, price, kilometers, userId, 
     *                 userFirstName, userLastName, userEmail
     */
    public static CarDetailDTO mapToCarDetail(Object[] row) {
        if (row == null || row.length < 9) {
            return null;
        }
        
        CarDetailDTO dto = new CarDetailDTO();
        dto.setCarId(((Number) row[0]).longValue());
        dto.setModelYear(((Number) row[1]).intValue());
        dto.setModel((String) row[2]);
        dto.setPrice(convertToBigDecimal(row[3]));
        dto.setKilometers(((Number) row[4]).intValue());
        dto.setUserId(((Number) row[5]).longValue());
        dto.setUserFirstName((String) row[6]);
        dto.setUserLastName((String) row[7]);
        dto.setUserEmail((String) row[8]);
        
        return dto;
    }

    /**
     * Maps Object[] array to UserListingStatsDTO
     * Expected order: userId, firstName, lastName, email, carCount, totalCarValue, averageCarPrice
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
        dto.setCarCount(((Number) row[4]).longValue());
        dto.setTotalCarValue(convertToBigDecimal(row[5]));
        dto.setAverageCarPrice(convertToBigDecimal(row[6]));
        
        return dto;
    }

    /**
     * Maps Object[] array for top cars by year query
     * Expected order: carId, modelYear, model, price, kilometers, 
     *                 userFirstName, userLastName, userEmail, rankInYear
     */
    public static CarDetailDTO mapToTopCarByYear(Object[] row) {
        if (row == null || row.length < 9) {
            return null;
        }
        
        CarDetailDTO dto = new CarDetailDTO();
        dto.setCarId(((Number) row[0]).longValue());
        dto.setModelYear(((Number) row[1]).intValue());
        dto.setModel((String) row[2]);
        dto.setPrice(convertToBigDecimal(row[3]));
        dto.setKilometers(((Number) row[4]).intValue());
        dto.setUserFirstName((String) row[5]);
        dto.setUserLastName((String) row[6]);
        dto.setUserEmail((String) row[7]);
        // rankInYear is at index 8, but not stored in DTO
        
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
    public static List<YearStatisticsDTO> mapToYearStatisticsList(List<Object[]> rows) {
        List<YearStatisticsDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            YearStatisticsDTO dto = mapToYearStatistics(row);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    public static List<YearStatisticsDTO> mapToYearStatisticsListSimple(List<Object[]> rows) {
        List<YearStatisticsDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            YearStatisticsDTO dto = mapToYearStatisticsSimple(row);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    public static List<CarDetailDTO> mapToCarDetailList(List<Object[]> rows) {
        List<CarDetailDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            CarDetailDTO dto = mapToCarDetail(row);
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



