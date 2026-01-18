-- Create database if not exists
CREATE DATABASE IF NOT EXISTS MiniSahibinden;
USE MiniSahibinden;

-- Drop old tables from previous schema (if they exist)
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS house;

-- Drop tables in correct order (due to foreign keys)
DROP TABLE IF EXISTS Favorites;
DROP TABLE IF EXISTS Vehicles;
DROP TABLE IF EXISTS RealEstate;
DROP TABLE IF EXISTS Listings;
DROP TABLE IF EXISTS Users;

-- Users table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Base Listings table (common fields for all listing types)
CREATE TABLE Listings (
    listing_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    listing_date DATE NOT NULL,
    category ENUM('Vehicle', 'RealEstate') NOT NULL,
    status ENUM('Active', 'Sold', 'Deleted') DEFAULT 'Active',
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Vehicles table (extends Listings for car/vehicle specific fields)
CREATE TABLE Vehicles (
    listing_id INT PRIMARY KEY,
    model_year INT NOT NULL,
    model_name VARCHAR(150) NOT NULL,
    kilometers INT NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES Listings(listing_id) ON DELETE CASCADE
);

-- RealEstate table (extends Listings for house/property specific fields)
CREATE TABLE RealEstate (
    listing_id INT PRIMARY KEY,
    seller_type VARCHAR(50),
    area_sqm INT NOT NULL,
    room_config VARCHAR(20),
    city VARCHAR(50) NOT NULL,
    district VARCHAR(50),
    neighborhood VARCHAR(100),
    FOREIGN KEY (listing_id) REFERENCES Listings(listing_id) ON DELETE CASCADE
);

-- Favorites table (many-to-many relationship between Users and Listings)
CREATE TABLE Favorites (
    user_id INT NOT NULL,
    listing_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, listing_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (listing_id) REFERENCES Listings(listing_id) ON DELETE CASCADE
);
