package com.rafirvan.minitrack.config;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeederService {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public SeederService(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void seedTaxTypes() {
        entityManager.createNativeQuery("INSERT INTO tax_types (name, multiplier) VALUES (?, ?) " +
                        "ON CONFLICT (name) DO NOTHING")
                .setParameter(1, "PPh")
                .setParameter(2, 0.1)
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO tax_types (name, multiplier) VALUES (?, ?) " +
                        "ON CONFLICT (name) DO NOTHING")
                .setParameter(1, "PPN")
                .setParameter(2, 0.05)
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO tax_types (name, multiplier) VALUES (?, ?) " +
                        "ON CONFLICT (name) DO NOTHING")
                .setParameter(1, "PBB")
                .setParameter(2, 0.015)
                .executeUpdate();
    }

    @Transactional
    public void seedUsers() {
        String adminPassword = passwordEncoder.encode("admin123");
        entityManager.createNativeQuery("INSERT INTO user_accounts (username, password, role) VALUES (?, ?, ?) " +
                        "ON CONFLICT (username) DO NOTHING")
                .setParameter(1, "admin")
                .setParameter(2, adminPassword)
                .setParameter(3, "ROLE_ADMIN")
                .executeUpdate();

        String userPassword = passwordEncoder.encode("user123");
        entityManager.createNativeQuery("INSERT INTO user_accounts (username, password, role) VALUES (?, ?, ?) " +
                        "ON CONFLICT (username) DO NOTHING")
                .setParameter(1, "user")
                .setParameter(2, userPassword)
                .setParameter(3, "ROLE_USER")
                .executeUpdate();
    }
}
