package com.minisahibinden.util;

import com.github.javafaker.Faker;
import com.minisahibinden.entity.Category;
import com.minisahibinden.entity.Listing;
import com.minisahibinden.entity.User;
import com.minisahibinden.repository.CategoryRepository;
import com.minisahibinden.repository.ListingRepository;
import com.minisahibinden.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ListingRepository listingRepository;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.listingRepository = listingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run if we have fewer than 10 listings
        if (listingRepository.count() < 10) {
            
            System.out.println("STARTING REALISTIC PRICE GENERATION...");
            Faker faker = new Faker();
            Random random = new Random();

            // 1. Create Categories
            Category catRealEstate = new Category();
            catRealEstate.setName("Real Estate");
            catRealEstate.setDescription("Houses, Land, Villas");
            
            Category catVehicles = new Category();
            catVehicles.setName("Vehicles");
            catVehicles.setDescription("Cars, Motorcycles, Trucks");
            
            Category catElectronics = new Category();
            catElectronics.setName("Electronics");
            catElectronics.setDescription("Computers, Phones, Gadgets");
            
            List<Category> categories = categoryRepository.saveAll(List.of(catRealEstate, catVehicles, catElectronics));

            // 2. Create Users
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                User u = new User();
                u.setFirstName(faker.name().firstName());
                u.setLastName(faker.name().lastName());
                u.setEmail(faker.internet().emailAddress());
                users.add(u);
            }
            userRepository.saveAll(users);

            // 3. Generate Listings
            List<Listing> listings = new ArrayList<>();
            for (int i = 0; i < 300; i++) {
                Listing l = new Listing();
                
                Category randomCat = categories.get(random.nextInt(categories.size()));
                l.setCategory(randomCat);
                l.setUser(users.get(random.nextInt(users.size())));

                // PRICE LOGIC: 80% chance of a "Clean" number, 20% chance of "Messy"
                boolean isCleanPrice = random.nextInt(100) < 80;
                long rawPrice;

                if (randomCat.getName().equals("Real Estate")) {
                    l.setTitle(faker.address().cityName() + " " + faker.options().option("Apartment", "Villa"));
                    l.setDescription(faker.lorem().sentence(10));
                    
                    if (isCleanPrice) {
                        // Round to nearest 50,000 (e.g., 1,250,000)
                        rawPrice = faker.number().numberBetween(20, 500) * 50_000L;
                    } else {
                        // Completely random (e.g., 2,141,947)
                        rawPrice = faker.number().numberBetween(1_000_000, 25_000_000);
                    }

                } else if (randomCat.getName().equals("Vehicles")) {
                    l.setTitle(faker.number().numberBetween(2010, 2024) + " " + faker.options().option(" BMW", " Mercedes", " Fiat"));
                    l.setDescription("No accidents. " + faker.lorem().sentence());
                    
                    if (isCleanPrice) {
                        // Round to nearest 1,000 (e.g., 850,000)
                        rawPrice = faker.number().numberBetween(400, 3000) * 1000L;
                    } else {
                        rawPrice = faker.number().numberBetween(400_000, 3_000_000);
                    }

                } else {
                    l.setTitle(faker.commerce().productName());
                    l.setDescription(faker.lorem().paragraph(2));
                    
                    if (isCleanPrice) {
                        // Round to nearest 100 (e.g., 12,500)
                        rawPrice = faker.number().numberBetween(10, 900) * 100L;
                    } else {
                        rawPrice = faker.number().numberBetween(1000, 90000);
                    }
                }

                l.setPrice(BigDecimal.valueOf(rawPrice));
                listings.add(l);
            }

            listingRepository.saveAll(listings);
            System.out.println("SUCCESS: Generated 300 realistic listings!");
        }
    }
}