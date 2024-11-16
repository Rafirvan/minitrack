package com.rafirvan.minitrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecordResponse {
    private Long id;
    private Long taxRecordId;
    private String paymentDate;
    private String paymentStatus;
}

