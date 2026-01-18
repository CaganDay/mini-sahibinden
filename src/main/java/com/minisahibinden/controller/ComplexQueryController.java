package com.minisahibinden.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.minisahibinden.entity.Vehicle;
import com.minisahibinden.repository.VehicleRepository;

@Controller
@RequestMapping("/queries")
public class ComplexQueryController {

    private final VehicleRepository vehicleRepository;

    public ComplexQueryController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("")
    public String queriesHome(Model model) {
        List<Integer> years = vehicleRepository.getDistinctYears();
        model.addAttribute("years", years);
        return "queries";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Vehicles from Active Users
    // ---------------------------------------------------------
    @GetMapping("/active-users")
    public String findVehiclesFromActiveUsers(
            @RequestParam(name = "minListingCount", defaultValue = "2") int minListingCount,
            Model model) {
        
        List<Vehicle> vehicles = vehicleRepository.findVehiclesFromActiveUsersSQL(minListingCount);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("queryName", "Vehicles from Active Sellers");
        model.addAttribute("queryDescription", 
            "Shows vehicles from sellers who have posted at least " + minListingCount + " listings");
        model.addAttribute("minListingCount", minListingCount);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Vehicles Above Average Price
    // ---------------------------------------------------------
    @GetMapping("/above-average")
    public String findVehiclesAboveAveragePrice(Model model) {
        
        List<Vehicle> vehicles = vehicleRepository.findVehiclesAboveAveragePriceSQL();
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("queryName", "Vehicles Above Average Price");
        model.addAttribute("queryDescription", 
            "Shows vehicles where the price is above the overall average price");
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Vehicles by Price and User
    // ---------------------------------------------------------
    @GetMapping("/price-user-filter")
    public String findVehiclesByPriceAndUser(
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "userNamePattern", required = false) String userNamePattern,
            Model model) {
        
        // Set default values if not provided
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("10000000");
        if (userNamePattern == null || userNamePattern.isEmpty()) userNamePattern = "";
        
        List<Vehicle> vehicles = vehicleRepository.findVehiclesByPriceAndUserSQL(minPrice, maxPrice, userNamePattern);
        
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("queryName", "Vehicles by Price Range and Seller");
        model.addAttribute("queryDescription", 
            "Shows vehicles within price range, posted by sellers matching name pattern");
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("userNamePattern", userNamePattern);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Top Vehicles Per Year
    // ---------------------------------------------------------
    @GetMapping("/top-per-year")
    public String findTopVehiclesPerYear(
            @RequestParam(name = "topN", defaultValue = "3") int topN,
            Model model) {
        
        List<Vehicle> vehicles = vehicleRepository.findTopVehiclesPerYearSQL(topN);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("queryName", "Top " + topN + " Vehicles Per Year");
        model.addAttribute("queryDescription", 
            "Shows the top " + topN + " vehicles per model year ranked by price");
        model.addAttribute("topN", topN);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Complex Multi-Condition Filter
    // ---------------------------------------------------------
    @GetMapping("/complex-filter")
    public String findVehiclesWithComplexFilter(
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
        
        List<Vehicle> vehicles = vehicleRepository.findVehiclesWithComplexFilterSQL(
            minYear, maxYear, minPrice, maxPrice, maxKilometers, keyword, sortBy);
        
        List<Integer> years = vehicleRepository.getDistinctYears();
        
        model.addAttribute("vehicles", vehicles);
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



