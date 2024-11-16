package com.rafirvan.minitrack.controller;

import com.rafirvan.minitrack.dto.response.PaymentRecordResponse;
import com.rafirvan.minitrack.service.PaymentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-records")
@RequiredArgsConstructor
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    @Operation(summary = "Change payment status to paid")
    @PutMapping("/{paymentRecordId}/mark-paid")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentRecordResponse> markAsPaid(@PathVariable Long paymentRecordId) {
        PaymentRecordResponse updatedRecord = paymentRecordService.markAsPaid(paymentRecordId);
        return ResponseEntity.ok(updatedRecord);
    }

    @Operation(summary = "Get payment record by ID")
    @GetMapping("/{paymentRecordId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentRecordResponse> getPaymentRecordById(@PathVariable Long paymentRecordId) {
        PaymentRecordResponse paymentRecord = paymentRecordService.getPaymentRecordById(paymentRecordId);
        return ResponseEntity.ok(paymentRecord);
    }
}
