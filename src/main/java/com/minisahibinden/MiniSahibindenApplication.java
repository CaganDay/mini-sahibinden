// go to http://localhost:8080/
package com.minisahibinden;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.minisahibinden.entity")
@EnableJpaRepositories(basePackages = "com.minisahibinden.repository")
public class MiniSahibindenApplication {

	public static void main(String[] args) {
		ensureDatabaseExists();
		SpringApplication.run(MiniSahibindenApplication.class, args);
	}

	private static void ensureDatabaseExists() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "MiniSahibinden";
		String user = "root";
		String pass = "Merrow4863";
		try (Connection conn = DriverManager.getConnection(url, user, pass);
			 Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
			System.out.println("Database checked/created: " + dbName);
		} catch (SQLException e) {
			System.err.println("Could not create database: " + e.getMessage());
		}
	}

}
