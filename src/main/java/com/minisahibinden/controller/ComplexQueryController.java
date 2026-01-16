package com.minisahibinden.controller;

import com.minisahibinden.entity.Category;
import com.minisahibinden.entity.Listing;
import com.minisahibinden.repository.CategoryRepository;
import com.minisahibinden.repository.ListingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/queries")
public class ComplexQueryController {

    private final ListingRepository listingRepository;
    private final CategoryRepository categoryRepository;

    public ComplexQueryController(ListingRepository listingRepository, CategoryRepository categoryRepository) {
        this.listingRepository = listingRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("")
    public String queriesHome(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "queries";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 1: Listings from Active Users
    // ---------------------------------------------------------
    @GetMapping("/active-users")
    public String findListingsFromActiveUsers(
            @RequestParam(name = "minListingCount", defaultValue = "2") int minListingCount,
            Model model) {
        
        List<Listing> listings = listingRepository.findListingsFromActiveUsersSQL(minListingCount);
        model.addAttribute("listings", listings);
        model.addAttribute("queryName", "Listings from Active Users");
        model.addAttribute("queryDescription", 
            "Shows listings from users who have posted at least " + minListingCount + " listings");
        model.addAttribute("minListingCount", minListingCount);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 2: Listings Above Category Average
    // ---------------------------------------------------------
    @GetMapping("/above-average")
    public String findListingsAboveCategoryAverage(Model model) {
        
        List<Listing> listings = listingRepository.findListingsAboveCategoryAverageSQL();
        model.addAttribute("listings", listings);
        model.addAttribute("queryName", "Listings Above Category Average Price");
        model.addAttribute("queryDescription", 
            "Shows listings where the price is above the average price of their category");
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 3: Listings by Price and User
    // ---------------------------------------------------------
    @GetMapping("/price-user-filter")
    public String findListingsByPriceAndUser(
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "userNamePattern", required = false) String userNamePattern,
            @RequestParam(name = "daysBack", defaultValue = "30") int daysBack,
            Model model) {
        
        // Set default values if not provided
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("1000000");
        if (userNamePattern == null || userNamePattern.isEmpty()) userNamePattern = "";
        
        List<Listing> listings = listingRepository.findListingsByPriceAndUserSQL(
            minPrice, maxPrice, userNamePattern, daysBack);
        
        model.addAttribute("listings", listings);
        model.addAttribute("queryName", "Listings by Price Range and User");
        model.addAttribute("queryDescription", 
            "Shows listings within price range, posted by users matching name pattern, within last " + daysBack + " days");
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("userNamePattern", userNamePattern);
        model.addAttribute("daysBack", daysBack);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 4: Top Listings Per Category
    // ---------------------------------------------------------
    @GetMapping("/top-per-category")
    public String findTopListingsPerCategory(
            @RequestParam(name = "topN", defaultValue = "3") int topN,
            Model model) {
        
        List<Listing> listings = listingRepository.findTopListingsPerCategorySQL(topN);
        model.addAttribute("listings", listings);
        model.addAttribute("queryName", "Top " + topN + " Listings Per Category");
        model.addAttribute("queryDescription", 
            "Shows the top " + topN + " listings per category ranked by price and date");
        model.addAttribute("topN", topN);
        
        return "query-results";
    }

    // ---------------------------------------------------------
    // COMPLEX QUERY 5: Complex Multi-Condition Filter
    // ---------------------------------------------------------
    @GetMapping("/complex-filter")
    public String findListingsWithComplexFilter(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
            Model model) {
        
        // Set default values
        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("1000000");
        if (startDateStr == null || startDateStr.isEmpty()) {
            startDateStr = LocalDate.now().minusYears(1).toString();
        }
        if (endDateStr == null || endDateStr.isEmpty()) {
            endDateStr = LocalDate.now().toString();
        }
        if (keyword == null) keyword = "";
        
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        List<Listing> listings = listingRepository.findListingsWithComplexFilterSQL(
            categoryId, minPrice, maxPrice, startDate, endDate, keyword, sortBy);
        
        List<Category> categories = categoryRepository.findAll();
        
        model.addAttribute("listings", listings);
        model.addAttribute("categories", categories);
        model.addAttribute("queryName", "Complex Multi-Condition Filter");
        model.addAttribute("queryDescription", 
            "Advanced filtering with category, price range, date range, keyword search, and sorting");
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        
        return "query-results";
    }
}



