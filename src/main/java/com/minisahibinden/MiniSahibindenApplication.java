// go to http://localhost:8080/
package com.minisahibinden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.minisahibinden.entity")
@EnableJpaRepositories(basePackages = "com.minisahibinden.repository")
public class MiniSahibindenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniSahibindenApplication.class, args);
	}

}
