package com.rafirvan.minitrack.service;

import com.rafirvan.minitrack.dto.request.TaxTypeRequest;
import com.rafirvan.minitrack.dto.response.TaxTypeResponse;

public interface TaxTypeService {
    TaxTypeResponse createTaxType(TaxTypeRequest request);
    TaxTypeResponse updateTaxType(Long id, TaxTypeRequest request);
    TaxTypeResponse getTaxTypeByName(String name);
}
