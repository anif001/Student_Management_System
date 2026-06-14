package com.studentmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "status", "SUCCESS",
                "message", "Student Management System Backend Running",
                "timestamp", LocalDateTime.now()
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "Student Management System"
        );
    }
}