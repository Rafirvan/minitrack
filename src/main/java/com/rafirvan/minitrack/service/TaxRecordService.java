package com.rafirvan.minitrack.service;

import com.rafirvan.minitrack.dto.request.TaxRecordRequest;
import com.rafirvan.minitrack.dto.response.TaxRecordResponse;

import java.util.List;

public interface TaxRecordService {

    List<TaxRecordResponse> getAllTaxRecordsByUser(Long userId);

    Double getTotalUnpaidTaxRecords(Long userId);

    Double getTotalTaxRecords(Long userId);

    TaxRecordResponse createTaxRecord(Long userId, TaxRecordRequest taxRecordRequest);
}
