package com.rafirvan.minitrack.controller;

import com.rafirvan.minitrack.dto.request.TaxRecordRequest;
import com.rafirvan.minitrack.dto.response.TaxRecordResponse;
import com.rafirvan.minitrack.service.TaxRecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tax-records")
@RequiredArgsConstructor
public class TaxRecordController {

    private final TaxRecordService taxRecordService;

    @Operation(summary = "Get all tax records by user Account id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaxRecordResponse>> getAllTaxRecordsByUser(@PathVariable Long userId) {
        List<TaxRecordResponse> records = taxRecordService.getAllTaxRecordsByUser(userId);
        return ResponseEntity.ok(records);
    }

    @Operation(summary = "Get the total amount of unpaid tax records by user Account id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}/total-unpaid")
    public ResponseEntity<Double> getTotalUnpaidTaxRecords(@PathVariable Long userId) {
        Double totalUnpaid = taxRecordService.getTotalUnpaidTaxRecords(userId);
        return ResponseEntity.ok(totalUnpaid);
    }

    @Operation(summary = "Get the total amount of all tax records by user Account id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalTaxRecords(@PathVariable Long userId) {
        Double total = taxRecordService.getTotalTaxRecords(userId);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Create a tax record to user Account by id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/user/{userId}")
    public ResponseEntity<TaxRecordResponse> createTaxRecord(
            @PathVariable Long userId,
            @RequestBody TaxRecordRequest request) {
        TaxRecordResponse createdRecord = taxRecordService.createTaxRecord(userId, request);
        return ResponseEntity.ok(createdRecord);
    }
}
