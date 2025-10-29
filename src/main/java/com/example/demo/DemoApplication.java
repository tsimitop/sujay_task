package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
	}
// jdbc:h2:mem:testdb for testing on the browser
}
