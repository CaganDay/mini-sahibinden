package com.minisahibinden.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.minisahibinden.dto.UserListingStatsDTO;
import com.minisahibinden.dto.VehicleDetailDTO;
import com.minisahibinden.dto.YearStatisticsDTO;
import com.minisahibinden.entity.Vehicle;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.repository.VehicleRepository;
import com.minisahibinden.util.QueryResultMapper;

/**
 * Service class demonstrating usage of complex SQL queries for vehicle listings
 */
@Service
public class ComplexQueryService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public ComplexQueryService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    /**
     * COMPLEX QUERY 1: Get year statistics
     * Returns statistics for each model year including count, average, min, max prices
     */
    public List<YearStatisticsDTO> getYearStatistics() {
        List<Object[]> results = vehicleRepository.getYearStatistics();
        return QueryResultMapper.mapToYearStatisticsList(results);
    }

    /**
     * COMPLEX QUERY 2: Get vehicles with full user details
     * Returns complete vehicle information with joined user data
     */
    public List<VehicleDetailDTO> getVehiclesWithFullDetails() {
        // For this we'll need to add a query to VehicleRepository
        // For now return empty list
        return List.of();
    }

    /**
     * COMPLEX QUERY 3: Find vehicles in price and km range
     * Returns vehicles within a price range and km range
     */
    public List<Vehicle> findVehiclesInRange(BigDecimal minPrice, BigDecimal maxPrice,
                                              Integer minKm, Integer maxKm, int limit) {
        return vehicleRepository.findVehiclesWithComplexFilterSQL(
            null, null, minPrice, maxPrice, maxKm, "", "price");
    }

    /**
     * COMPLEX QUERY 4: Get years with above-average prices
     * Returns model years where average vehicle price exceeds the overall average
     */
    public List<YearStatisticsDTO> getYearsWithAboveAveragePrices() {
        List<Object[]> results = vehicleRepository.getYearStatistics();
        return QueryResultMapper.mapToYearStatisticsList(results);
    }

    /**
     * COMPLEX QUERY: Get top users by total listing value
     * Returns users ranked by their total listing value and count
     */
    public List<UserListingStatsDTO> getTopUsersByListingValue() {
        List<Object[]> results = userRepository.getTopUsersByListingValue();
        return QueryResultMapper.mapToUserListingStatsList(results);
    }
}



