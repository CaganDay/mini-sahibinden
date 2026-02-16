package com.minisahibinden.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.minisahibinden.entity.User;
import com.minisahibinden.repository.ListingRepository;
import com.minisahibinden.repository.RealEstateRepository;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.repository.VehicleRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final RealEstateRepository realEstateRepository;
    private final ListingRepository listingRepository;

    public DataSeeder(UserRepository userRepository, VehicleRepository vehicleRepository,
                     RealEstateRepository realEstateRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.realEstateRepository = realEstateRepository;
        this.listingRepository = listingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only create users if database is empty
        if (userRepository.count() == 0) {
            System.out.println("Creating sample users...");
            Faker faker = new Faker();

            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                User u = new User(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone(),
                    String.valueOf(i + 1)  // Password is set to user index
                );
                users.add(u);
            }
            userRepository.saveAll(users);
            System.out.println("Created " + users.size() + " users!");
        }

        // Data is automatically loaded from data.sql by Spring Boot
        long totalListings = listingRepository.count();
        long vehicleCount = vehicleRepository.count();
        long realEstateCount = realEstateRepository.count();
        
        System.out.println("===========================================");
        System.out.println("Database Summary:");
        System.out.println("  - Total listings: " + totalListings);
        System.out.println("  - Vehicles: " + vehicleCount);
        System.out.println("  - Real Estate: " + realEstateCount);
        System.out.println("===========================================");
    }
}
