package com.minisahibinden.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.minisahibinden.dto.CarDetailDTO;
import com.minisahibinden.dto.UserListingStatsDTO;
import com.minisahibinden.dto.YearStatisticsDTO;
import com.minisahibinden.entity.Car;
import com.minisahibinden.repository.CarRepository;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.util.QueryResultMapper;

/**
 * Service class demonstrating usage of complex SQL queries for car listings
 */
@Service
public class ComplexQueryService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public ComplexQueryService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    /**
     * COMPLEX QUERY 1: Get year statistics
     * Returns statistics for each model year including count, average, min, max prices
     */
    public List<YearStatisticsDTO> getYearStatistics() {
        List<Object[]> results = carRepository.getYearStatistics();
        return QueryResultMapper.mapToYearStatisticsList(results);
    }

    /**
     * COMPLEX QUERY 2: Get cars with full user details
     * Returns complete car information with joined user data
     */
    public List<CarDetailDTO> getCarsWithFullDetails() {
        List<Object[]> results = carRepository.getCarsWithFullDetails();
        return QueryResultMapper.mapToCarDetailList(results);
    }

    /**
     * COMPLEX QUERY 3: Find cars in price and km range
     * Returns cars within a price range and km range
     */
    public List<Car> findCarsInRange(BigDecimal minPrice, BigDecimal maxPrice,
                                      Integer minKm, Integer maxKm, int limit) {
        return carRepository.findCarsInRange(minPrice, maxPrice, minKm, maxKm, limit);
    }

    /**
     * COMPLEX QUERY 4: Get years with above-average prices
     * Returns model years where average car price exceeds the overall average
     */
    public List<YearStatisticsDTO> getYearsWithAboveAveragePrices() {
        List<Object[]> results = carRepository.getYearsWithAboveAveragePrices();
        return QueryResultMapper.mapToYearStatisticsListSimple(results);
    }

    /**
     * COMPLEX QUERY 5: Get top cars by year with ranking
     * Returns top cars per model year with user information and ranking
     */
    public List<CarDetailDTO> getTopCarsByYearWithRanking() {
        List<Object[]> results = carRepository.getTopCarsByYearWithRanking();
        return results.stream()
                .map(QueryResultMapper::mapToTopCarByYear)
                .collect(Collectors.toList());
    }

    /**
     * COMPLEX QUERY: Get top users by total car value
     * Returns users ranked by their total car value and car count
     */
    public List<UserListingStatsDTO> getTopUsersByCarValue(int limit) {
        List<Object[]> results = userRepository.getTopUsersByCarValue(limit);
        return QueryResultMapper.mapToUserListingStatsList(results);
    }
}



