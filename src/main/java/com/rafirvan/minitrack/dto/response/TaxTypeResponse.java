package com.rafirvan.minitrack.dto.response;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxTypeResponse {
    private Long id;
    private String name;
    private Double multiplier;
}
