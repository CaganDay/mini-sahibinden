package com.minisahibinden.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.minisahibinden.entity.Car;
import com.minisahibinden.repository.CarRepository;

@Controller
@RequestMapping("/queries")
public class ComplexQueryController {

    private final CarRepository carRepository;

    public ComplexQueryController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("")
    public String queriesHome(Model model) {
        List<Integer> years = carRepository.getDistinctYears();
        model.addAttribute("years", years);
        return "queries";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Cars from Active Users
    // ---------------------------------------------------------
    @GetMapping("/active-users")
    public String findCarsFromActiveUsers(
            @RequestParam(name = "minCarCount", defaultValue = "2") int minCarCount,
            Model model) {
        
        List<Car> cars = carRepository.findCarsFromActiveUsersSQL(minCarCount);
        model.addAttribute("cars", cars);
        model.addAttribute("queryName", "Cars from Active Sellers");
        model.addAttribute("queryDescription", 
            "Shows cars from sellers who have posted at least " + minCarCount + " cars");
        model.addAttribute("minCarCount", minCarCount);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Cars Above Average Price
    // ---------------------------------------------------------
    @GetMapping("/above-average")
    public String findCarsAboveAveragePrice(Model model) {
        
        List<Car> cars = carRepository.findCarsAboveAveragePriceSQL();
        model.addAttribute("cars", cars);
        model.addAttribute("queryName", "Cars Above Average Price");
        model.addAttribute("queryDescription", 
            "Shows cars where the price is above the overall average price");
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Cars by Price and User
    // ---------------------------------------------------------
    @GetMapping("/price-user-filter")
    public String findCarsByPriceAndUser(
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "userNamePattern", required = false) String userNamePattern,
            Model model) {
        
        // Set default values if not provided
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("10000000");
        if (userNamePattern == null || userNamePattern.isEmpty()) userNamePattern = "";
        
        List<Car> cars = carRepository.findCarsByPriceAndUserSQL(minPrice, maxPrice, userNamePattern);
        
        model.addAttribute("cars", cars);
        model.addAttribute("queryName", "Cars by Price Range and Seller");
        model.addAttribute("queryDescription", 
            "Shows cars within price range, posted by sellers matching name pattern");
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("userNamePattern", userNamePattern);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Top Cars Per Year
    // ---------------------------------------------------------
    @GetMapping("/top-per-year")
    public String findTopCarsPerYear(
            @RequestParam(name = "topN", defaultValue = "3") int topN,
            Model model) {
        
        List<Car> cars = carRepository.findTopCarsPerYearSQL(topN);
        model.addAttribute("cars", cars);
        model.addAttribute("queryName", "Top " + topN + " Cars Per Year");
        model.addAttribute("queryDescription", 
            "Shows the top " + topN + " cars per model year ranked by price");
        model.addAttribute("topN", topN);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Complex Multi-Condition Filter
    // ---------------------------------------------------------
    @GetMapping("/complex-filter")
    public String findCarsWithComplexFilter(
            @RequestParam(name = "minYear", required = false) Integer minYear,
            @RequestParam(name = "maxYear", required = false) Integer maxYear,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "maxKilometers", required = false) Integer maxKilometers,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "price") String sortBy,
            Model model) {
        
        // Set default values
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("10000000");
        if (keyword == null) keyword = "";
        
        List<Car> cars = carRepository.findCarsWithComplexFilterSQL(
            minYear, maxYear, minPrice, maxPrice, maxKilometers, keyword, sortBy);
        
        List<Integer> years = carRepository.getDistinctYears();
        
        model.addAttribute("cars", cars);
        model.addAttribute("years", years);
        model.addAttribute("queryName", "Complex Multi-Condition Filter");
        model.addAttribute("queryDescription", 
            "Advanced filtering with year range, price range, kilometers, keyword search, and sorting");
        model.addAttribute("minYear", minYear);
        model.addAttribute("maxYear", maxYear);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("maxKilometers", maxKilometers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        
        return "query-results";
    }
}



