package com.minisahibinden.controller;

import com.minisahibinden.entity.Category;
import com.minisahibinden.entity.Listing;
import com.minisahibinden.repository.CategoryRepository;
import com.minisahibinden.repository.ListingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    // 1. Define the Repositories we need
    private final CategoryRepository categoryRepository;
    private final ListingRepository listingRepository;

    // 2. Constructor Injection (Spring automatically connects them here)
    public HomeController(CategoryRepository categoryRepository, ListingRepository listingRepository) {
        this.categoryRepository = categoryRepository;
        this.listingRepository = listingRepository;
    }

    @GetMapping("/")
    public String home(Model model, 
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "categoryId", required = false) Long categoryId) {
        
        List<Category> categories = categoryRepository.findAll();
        List<Listing> listings;

        if (keyword != null && !keyword.isEmpty()) {
            // Case 1: Use our CUSTOM SQL for searching
            listings = listingRepository.searchListingsSQL(keyword);
            
        } else if (categoryId != null) {
            // Case 2: Use our CUSTOM SQL for filtering
            listings = listingRepository.filterByCategorySQL(categoryId);
            
        } else {
            // Case 3: Standard Find All (Still using JPA default here)
            listings = listingRepository.findAll();
        }

        model.addAttribute("categories", categories);
        model.addAttribute("listings", listings);
        
        return "home";
    }

    @GetMapping("/listing/{id}")
    public String listingDetail(@PathVariable Long id, Model model) {
        // 1. Try to find the listing in the DB by ID
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid listing Id:" + id));

        // 2. Add it to the model so HTML can see it
        model.addAttribute("listing", listing);

        // 3. Return the new HTML file name
        return "listing-detail";
    }
}