package com.example.book_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookWebApplication.class, args);
	}

}
