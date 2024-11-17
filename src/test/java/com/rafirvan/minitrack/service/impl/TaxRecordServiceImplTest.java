package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.PaymentStatus;
import com.rafirvan.minitrack.dto.request.TaxRecordRequest;
import com.rafirvan.minitrack.dto.response.TaxRecordResponse;
import com.rafirvan.minitrack.dto.response.TaxTypeResponse;
import com.rafirvan.minitrack.service.TaxTypeService;
import com.rafirvan.minitrack.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxRecordServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private TaxTypeService taxTypeService;

    @Mock
    private Query query;

    @InjectMocks
    private TaxRecordServiceImpl taxRecordService;

    private static final Long USER_ID = 1L;
    private static final Long TAX_TYPE_ID = 1L;
    private static final String TAX_TYPE_NAME = "ABCD";
    private static final Double AMOUNT_1 = 1000.0;
    private static final Double AMOUNT_2 = 2000.0;
    private static final Double TAXED_AMOUNT_1 = 200.0;
    private static final Double TAXED_AMOUNT_2 = 400.0;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
    }

    @Test
    void shouldReturnListOfTaxRecords_WhenGetAllTaxRecordsByUser() {
        Object[] paidRecord = {
                1L, USER_ID, TAX_TYPE_NAME, AMOUNT_1, TAXED_AMOUNT_1,
                Timestamp.valueOf(NOW.minusDays(2)), PaymentStatus.PAID.toString()
        };
        Object[] unpaidRecord = {
                2L, USER_ID, TAX_TYPE_NAME, AMOUNT_2, TAXED_AMOUNT_2,
                Timestamp.valueOf(NOW), PaymentStatus.UNPAID.toString()
        };
        List<Object[]> mockResults = Arrays.asList(paidRecord, unpaidRecord);
        when(query.getResultList()).thenReturn(mockResults);

        List<TaxRecordResponse> result = taxRecordService.getAllTaxRecordsByUser(USER_ID);

        assertNotNull(result);
        assertEquals(2, result.size());

        TaxRecordResponse paidResponse = result.get(0);
        assertEquals(1L, paidResponse.getId());
        assertEquals(USER_ID, paidResponse.getUserId());
        assertEquals(TAX_TYPE_NAME, paidResponse.getTaxTypeName());
        assertEquals(AMOUNT_1, paidResponse.getAmount());
        assertEquals(TAXED_AMOUNT_1, paidResponse.getTaxedAmount());
        assertEquals(PaymentStatus.PAID.toString(), paidResponse.getPaymentStatus());

        TaxRecordResponse unpaidResponse = result.get(1);
        assertEquals(2L, unpaidResponse.getId());
        assertEquals(USER_ID, unpaidResponse.getUserId());
        assertEquals(TAX_TYPE_NAME, unpaidResponse.getTaxTypeName());
        assertEquals(AMOUNT_2, unpaidResponse.getAmount());
        assertEquals(TAXED_AMOUNT_2, unpaidResponse.getTaxedAmount());
        assertEquals(PaymentStatus.UNPAID.toString(), unpaidResponse.getPaymentStatus());

        verify(entityManager).createNativeQuery(anyString());
        verify(query).setParameter(1, USER_ID);
    }

    @Test
    void shouldReturnOnlyUnpaidAmount_getTotalUnpaidTaxRecords() {
        when(query.getSingleResult()).thenReturn(TAXED_AMOUNT_2);

        Double result = taxRecordService.getTotalUnpaidTaxRecords(USER_ID);

        assertEquals(TAXED_AMOUNT_2, result);
        verify(entityManager).createNativeQuery(anyString());
        verify(query).setParameter(1, USER_ID);
    }

    @Test
    void shouldReturnTotalAmount_WhenGetTotalTaxRecords() {
        Double totalAmount = TAXED_AMOUNT_1 + TAXED_AMOUNT_2;
        when(query.getSingleResult()).thenReturn(totalAmount);

        Double result = taxRecordService.getTotalTaxRecords(USER_ID);

        assertEquals(totalAmount, result);
        verify(entityManager).createNativeQuery(anyString());
        verify(query).setParameter(1, USER_ID);
    }

    @Test
    void shouldCreateNewTaxRecord_WhenCreateTaxRecord() {
        TaxRecordRequest request = new TaxRecordRequest();
        request.setTaxTypeName(TAX_TYPE_NAME);
        request.setAmount(AMOUNT_1);

        TaxTypeResponse taxTypeResponse = new TaxTypeResponse();
        taxTypeResponse.setId(TAX_TYPE_ID);
        taxTypeResponse.setName(TAX_TYPE_NAME);
        taxTypeResponse.setMultiplier(0.2);

        when(taxTypeService.getTaxTypeByName(TAX_TYPE_NAME)).thenReturn(taxTypeResponse);
        when(query.executeUpdate()).thenReturn(1);

        Object[] recordData = {1L, USER_ID, TAX_TYPE_NAME, AMOUNT_1, TAXED_AMOUNT_1,
                Timestamp.valueOf(NOW)};
        when(query.getSingleResult()).thenReturn(recordData);

        TaxRecordResponse result = taxRecordService.createTaxRecord(USER_ID, request);

        assertNotNull(result);
        assertEquals(TAX_TYPE_NAME, result.getTaxTypeName());
        assertEquals(AMOUNT_1, result.getAmount());
        assertEquals(TAXED_AMOUNT_1, result.getTaxedAmount());
        assertEquals(PaymentStatus.UNPAID.toString(), result.getPaymentStatus());

        verify(validationUtil).validate(request);
        verify(taxTypeService).getTaxTypeByName(TAX_TYPE_NAME);
        verify(entityManager, times(3)).createNativeQuery(anyString());
    }
}