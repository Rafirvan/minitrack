package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.dto.request.TaxTypeRequest;
import com.rafirvan.minitrack.dto.response.TaxTypeResponse;
import com.rafirvan.minitrack.service.TaxTypeService;
import com.rafirvan.minitrack.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TaxTypeServiceImpl implements TaxTypeService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TaxTypeResponse createTaxType(TaxTypeRequest request) {
        validationUtil.validate(request);
        entityManager.createNativeQuery("INSERT INTO tax_types (name, multiplier) VALUES (?, ?)")
                .setParameter(1, request.getName())
                .setParameter(2, request.getMultiplier())
                .executeUpdate();

        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, name, multiplier FROM tax_types WHERE name = ?")
                .setParameter(1, request.getName())
                .getSingleResult();

        return mapToResponse(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TaxTypeResponse updateTaxType(Long id, TaxTypeRequest request) {
        validationUtil.validate(request);
        int updated = entityManager.createNativeQuery(
                        "UPDATE tax_types SET name = ?, multiplier = ? WHERE id = ?")
                .setParameter(1, request.getName())
                .setParameter(2, request.getMultiplier())
                .setParameter(3, id)
                .executeUpdate();

        if (updated == 0) {
            throw new RuntimeException("Tax type not found for ID: " + id);
        }

        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, name, multiplier FROM tax_types WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();

        return mapToResponse(result);
    }

    @Transactional(readOnly = true)
    @Override
    public TaxTypeResponse getTaxTypeByName(String name) {
        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(
                            "SELECT id, name, multiplier FROM tax_types WHERE name = ?")
                    .setParameter(1, name)
                    .getSingleResult();
            return mapToResponse(result);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax type not found");
        }
    }



    private TaxTypeResponse mapToResponse(Object[] result) {
        TaxTypeResponse response = new TaxTypeResponse();
        response.setId(((Number) result[0]).longValue());
        response.setName((String) result[1]);
        response.setMultiplier((Double) result[2]);
        return response;
    }
}
