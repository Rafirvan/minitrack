package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.PaymentStatus;
import com.rafirvan.minitrack.dto.response.PaymentRecordResponse;
import com.rafirvan.minitrack.model.PaymentRecord;
import com.rafirvan.minitrack.model.TaxRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRecordServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private PaymentRecordServiceImpl paymentRecordService;

    private PaymentRecord paymentRecord;
    private TaxRecord taxRecord;
    private static final Long PAYMENT_RECORD_ID = 1L;
    private static final Long TAX_RECORD_ID = 1L;

    @BeforeEach
    void setUp() {
        taxRecord = new TaxRecord();
        taxRecord.setId(TAX_RECORD_ID);

        paymentRecord = new PaymentRecord();
        paymentRecord.setId(PAYMENT_RECORD_ID);
        paymentRecord.setTaxRecord(taxRecord);
        paymentRecord.setPaymentStatus(PaymentStatus.UNPAID);
    }

    @Test
    void shouldUpdateStatusToPaid_WhenStatusIsUnpaid() {
        when(entityManager.createNativeQuery(anyString(), eq(PaymentRecord.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(paymentRecord);

        PaymentRecordResponse response = paymentRecordService.markAsPaid(PAYMENT_RECORD_ID);

        assertNotNull(response);
        assertEquals(paymentRecord.getId(), response.getId());
        assertEquals(paymentRecord.getTaxRecord().getId(), response.getTaxRecordId());
        assertEquals(paymentRecord.getPaymentStatus().toString(), response.getPaymentStatus());
        assertEquals(paymentRecord.getPaymentDate().toString(), response.getPaymentDate());

        verify(entityManager).merge(paymentRecord);
        assertEquals(PaymentStatus.PAID, paymentRecord.getPaymentStatus());
        assertNotNull(paymentRecord.getPaymentDate());
    }

    @Test
    void shouldThrowException_WhenStatusIsPaid() {
        paymentRecord.setPaymentStatus(PaymentStatus.PAID);
        paymentRecord.setPaymentDate(LocalDateTime.now());
        when(entityManager.createNativeQuery(anyString(), eq(PaymentRecord.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(paymentRecord);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> paymentRecordService.markAsPaid(PAYMENT_RECORD_ID));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Payment record is not in UNPAID status.", exception.getReason());

        verify(entityManager, never()).merge(any());
    }

    @Test
    void shouldReturnPaymentRecord_WhenIdExists() {
        LocalDateTime paymentDate = LocalDateTime.now();
        paymentRecord.setPaymentStatus(PaymentStatus.PAID);
        paymentRecord.setPaymentDate(paymentDate);

        when(entityManager.createNativeQuery(anyString(), eq(PaymentRecord.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(paymentRecord);

        PaymentRecordResponse response = paymentRecordService.getPaymentRecordById(PAYMENT_RECORD_ID);

        assertNotNull(response);
        assertEquals(paymentRecord.getId(), response.getId());
        assertEquals(paymentRecord.getTaxRecord().getId(), response.getTaxRecordId());
        assertEquals(paymentRecord.getPaymentStatus().toString(), response.getPaymentStatus());
        assertEquals(paymentRecord.getPaymentDate().toString(), response.getPaymentDate());
    }
}