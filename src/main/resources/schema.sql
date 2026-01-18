-- Create database if not exists
CREATE DATABASE IF NOT EXISTS MiniSahibinden;

-- Use the database
USE MiniSahibinden;

-- Drop tables if they exist (in correct order due to foreign key)
DROP TABLE IF EXISTS house;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Create car table
CREATE TABLE car (
    id BIGINT NOT NULL AUTO_INCREMENT,
    model_year INT,
    model VARCHAR(255),
    price DECIMAL(38,2),
    kilometers INT,
    user_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_car_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- Create house table
CREATE TABLE house (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_type VARCHAR(50),
    square_meters DOUBLE,
    room_count VARCHAR(20),
    city VARCHAR(100),
    district VARCHAR(100),
    neighborhood VARCHAR(100),
    date_posted DATE,
    price DECIMAL(38,2),
    user_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_house_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;
