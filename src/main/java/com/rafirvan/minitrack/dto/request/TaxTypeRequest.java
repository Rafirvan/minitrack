package com.rafirvan.minitrack.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxTypeRequest {
    @NotBlank(message = "Tax type name is required")
    private String name;

    @NotNull(message = "Multiplier is required")
    private Double multiplier;
}

