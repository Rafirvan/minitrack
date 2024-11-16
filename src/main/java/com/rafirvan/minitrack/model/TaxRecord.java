package com.rafirvan.minitrack.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tax_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "tax_type_id", nullable = false)
    private TaxType taxType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double taxedAmount;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(mappedBy = "taxRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentRecord paymentRecord;
}
