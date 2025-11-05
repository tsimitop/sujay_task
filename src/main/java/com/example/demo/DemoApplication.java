package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
	}
    // for testing on the browser JDBC URL field:
    // jdbc:h2:mem:testdb (in-memory)
    // jdbc:h2:file:/data/testdb (filebased)
}
