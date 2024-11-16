package com.rafirvan.minitrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tax_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double multiplier;

}
