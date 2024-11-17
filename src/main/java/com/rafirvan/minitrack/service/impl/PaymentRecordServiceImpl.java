package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.PaymentStatus;
import com.rafirvan.minitrack.dto.response.PaymentRecordResponse;
import com.rafirvan.minitrack.model.PaymentRecord;
import com.rafirvan.minitrack.service.PaymentRecordService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public PaymentRecordResponse markAsPaid(Long paymentRecordId) {
        String fetchSql = "SELECT * FROM payment_records WHERE id = ?";
        PaymentRecord paymentRecord = (PaymentRecord) entityManager
                .createNativeQuery(fetchSql, PaymentRecord.class)
                .setParameter(1, paymentRecordId)
                .getSingleResult();

        if (paymentRecord.getPaymentStatus() != PaymentStatus.UNPAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment record is not in UNPAID status.");
        }

        paymentRecord.setPaymentStatus(PaymentStatus.PAID);
        paymentRecord.setPaymentDate(LocalDateTime.now());
        entityManager.merge(paymentRecord);

        return mapToResponse(paymentRecord);
    }

    @Override
    public PaymentRecordResponse getPaymentRecordById(Long paymentRecordId) {
        String fetchSql = "SELECT * FROM payment_records WHERE id = ?";
        PaymentRecord paymentRecord = (PaymentRecord) entityManager
                .createNativeQuery(fetchSql, PaymentRecord.class)
                .setParameter(1, paymentRecordId)
                .getSingleResult();

        return mapToResponse(paymentRecord);
    }

    private PaymentRecordResponse mapToResponse(PaymentRecord paymentRecord) {
        return new PaymentRecordResponse(
                paymentRecord.getId(),
                paymentRecord.getTaxRecord().getId(),
                paymentRecord.getPaymentDate() !=null ?paymentRecord.getPaymentDate().toString():null,
                paymentRecord.getPaymentStatus().toString()
        );
    }
}
