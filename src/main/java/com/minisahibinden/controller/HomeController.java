package com.minisahibinden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // 1. DUMMY CATEGORIES
        List<String> categories = List.of("Real Estate", "Vehicles", "Electronics", "Home & Garden");
        model.addAttribute("categories", categories);

        // 2. DUMMY ADVERTS (Simulating database rows)
        // In the future, this will come from 'advertRepository.findAll()'
        List<Map<String, Object>> dummyAds = new ArrayList<>();
        
        dummyAds.add(Map.of("title", "2015 Honda Civic Clean", "price", "850,000", "city", "Istanbul"));
        dummyAds.add(Map.of("title", "iPhone 13 Pro Max", "price", "42,000", "city", "Ankara"));
        dummyAds.add(Map.of("title", "Sea View Apartment", "price", "12,500,000", "city", "Izmir"));
        dummyAds.add(Map.of("title", "Gaming Laptop", "price", "25,000", "city", "Bursa"));
        dummyAds.add(Map.of("title", "Winter Tires (Set of 4)", "price", "4,000", "city", "Erzurum"));

        model.addAttribute("adverts", dummyAds);

        return "home";
    }
}