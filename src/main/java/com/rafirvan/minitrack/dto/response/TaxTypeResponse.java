package com.rafirvan.minitrack.dto.response;

import lombok.*;


@Data
public class TaxTypeResponse {
    private Long id;
    private String name;
    private Double multiplier;
}
