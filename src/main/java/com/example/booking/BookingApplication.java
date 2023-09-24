package com.example.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BookingApplication.class);
        application.setAdditionalProfiles("prod");
        application.run(args);
    }

}