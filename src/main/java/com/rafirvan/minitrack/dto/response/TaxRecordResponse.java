package com.rafirvan.minitrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRecordResponse {
    private Long id;
    private Long userId;
    private String taxTypeName;
    private Double amount;
    private Double taxedAmount;
    private String date;
    private String paymentStatus;
}
