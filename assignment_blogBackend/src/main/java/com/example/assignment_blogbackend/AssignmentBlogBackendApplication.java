package com.example.assignment_blogbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AssignmentBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentBlogBackendApplication.class, args);
    }

}
