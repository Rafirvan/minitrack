package com.rafirvan.minitrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final SeederService seederService;


    @PostConstruct
    public void seedData() {
        seederService.seedTaxTypes();
        seederService.seedUsers();
    }
}

