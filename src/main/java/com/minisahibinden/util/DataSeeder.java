package com.minisahibinden.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.minisahibinden.entity.User;
import com.minisahibinden.repository.CarRepository;
import com.minisahibinden.repository.HouseRepository;
import com.minisahibinden.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final HouseRepository houseRepository;

    public DataSeeder(UserRepository userRepository, CarRepository carRepository, HouseRepository houseRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.houseRepository = houseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only create users if database is empty
        if (userRepository.count() == 0) {
            System.out.println("Creating sample users...");
            Faker faker = new Faker();
            
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                User u = new User();
                u.setFirstName(faker.name().firstName());
                u.setLastName(faker.name().lastName());
                u.setEmail(faker.internet().emailAddress());
                users.add(u);
            }
            userRepository.saveAll(users);
            System.out.println("Created " + users.size() + " users!");
        }
        
        // Data is automatically loaded from data.sql by Spring Boot
        long carCount = carRepository.count();
        long houseCount = houseRepository.count();
        System.out.println("===========================================");
        System.out.println("Database Summary:");
        System.out.println("  - Total cars: " + carCount);
        System.out.println("  - Total houses: " + houseCount);
        System.out.println("===========================================");
    }
}