package com.example.findmeajob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.findmeajob.database")
@EntityScan("com.example.findmeajob.database")
public class FindmeajobApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindmeajobApplication.class, args);
	}

}
