package com.rafirvan.minitrack.controller;

import com.rafirvan.minitrack.dto.request.TaxTypeRequest;
import com.rafirvan.minitrack.dto.response.TaxTypeResponse;
import com.rafirvan.minitrack.service.TaxTypeService;
import com.rafirvan.minitrack.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tax Type Management")
@RestController
@RequestMapping("/api/tax-types")
@RequiredArgsConstructor
public class TaxTypeController {

    private final TaxTypeService taxTypeService;

    @Operation(summary = "Create a new tax type")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTaxType(@RequestBody TaxTypeRequest request) {
        TaxTypeResponse response = taxTypeService.createTaxType(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Tax type created successfully", response);
    }

    @Operation(summary = "Update an existing tax type by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaxType(@PathVariable Long id, @RequestBody TaxTypeRequest request) {
        TaxTypeResponse response = taxTypeService.updateTaxType(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Tax type updated successfully", response);
    }

    @Operation(summary = "Get a tax type by name")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getTaxTypeByName(@RequestParam String name) {
        TaxTypeResponse response = taxTypeService.getTaxTypeByName(name);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Tax type retrieved successfully", response);
    }
}
