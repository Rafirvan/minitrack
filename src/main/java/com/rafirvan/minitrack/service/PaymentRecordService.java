package com.rafirvan.minitrack.service;

import com.rafirvan.minitrack.dto.response.PaymentRecordResponse;

public interface PaymentRecordService {
    PaymentRecordResponse markAsPaid(Long paymentRecordId);
    PaymentRecordResponse getPaymentRecordById(Long paymentRecordId);
}
