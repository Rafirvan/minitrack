package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.PaymentStatus;
import com.rafirvan.minitrack.dto.request.TaxRecordRequest;
import com.rafirvan.minitrack.dto.response.TaxRecordResponse;
import com.rafirvan.minitrack.dto.response.TaxTypeResponse;
import com.rafirvan.minitrack.service.TaxRecordService;
import com.rafirvan.minitrack.service.TaxTypeService;
import com.rafirvan.minitrack.util.DateUtil;
import com.rafirvan.minitrack.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxRecordServiceImpl implements TaxRecordService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ValidationUtil validationUtil;
    private final TaxTypeService taxTypeService;

    @Transactional(readOnly = true)
    @Override
    public List<TaxRecordResponse> getAllTaxRecordsByUser(Long userId) {
        String sql = "SELECT tr.id, tr.user_id, tt.name, tr.amount, tr.taxed_amount, tr.date, pr.payment_status " +
                "FROM tax_records tr " +
                "JOIN tax_types tt ON tr.tax_type_id = tt.id " +
                "LEFT JOIN payment_records pr ON tr.id = pr.tax_record_id " +
                "WHERE tr.user_id = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(result -> new TaxRecordResponse(
                ((Number) result[0]).longValue(),
                ((Number) result[1]).longValue(),
                (String) result[2],
                (Double) result[3],
                (Double) result[4],
                DateUtil.localDateTimeToString(((Timestamp) result[5]).toLocalDateTime()),
                (String) result[6]
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Double getTotalUnpaidTaxRecords(Long userId) {
        String sql = "SELECT SUM(tr.taxed_amount) " +
                "FROM tax_records tr " +
                "LEFT JOIN payment_records pr ON tr.id = pr.tax_record_id " +
                "WHERE tr.user_id = ? AND pr.payment_status = 'UNPAID'";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);
        return (Double) query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public Double getTotalTaxRecords(Long userId) {
        String sql = "SELECT SUM(tr.taxed_amount) FROM tax_records tr WHERE tr.user_id = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, userId);
        return (Double) query.getSingleResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public TaxRecordResponse createTaxRecord(Long userId, TaxRecordRequest request) {
        validationUtil.validate(request);

        TaxTypeResponse taxType = taxTypeService.getTaxTypeByName(request.getTaxTypeName());
        Double taxedAmount = request.getAmount() * taxType.getMultiplier();

        String taxRecordSql = "INSERT INTO tax_records (user_id, tax_type_id, taxed_amount, amount, date) " +
                "VALUES (?, ?, ?, ?, ?)";
        Query taxRecordQuery = entityManager.createNativeQuery(taxRecordSql);
        taxRecordQuery.setParameter(1, userId);
        taxRecordQuery.setParameter(2, taxType.getId());
        taxRecordQuery.setParameter(3, taxedAmount);
        taxRecordQuery.setParameter(4, request.getAmount());
        taxRecordQuery.setParameter(5,(LocalDateTime.now()));
        int rowsAffected = taxRecordQuery.executeUpdate();

        if (rowsAffected > 0) {
            String fetchSql = "SELECT tr.id, tr.user_id, tt.name, tr.amount, tr.taxed_amount, tr.date " +
                    "FROM tax_records tr JOIN tax_types tt ON tr.tax_type_id = tt.id " +
                    "WHERE tr.user_id = ? AND tr.tax_type_id = ?";
            Query fetchQuery = entityManager.createNativeQuery(fetchSql);
            fetchQuery.setParameter(1, userId);
            fetchQuery.setParameter(2, taxType.getId());
            Object[] result = (Object[]) fetchQuery.getSingleResult();

            String paymentRecordSql = "INSERT INTO payment_records (tax_record_id, payment_date, payment_status) " +
                    "VALUES (?, ?, ?)";
            Query paymentRecordQuery = entityManager.createNativeQuery(paymentRecordSql);
            paymentRecordQuery.setParameter(1, ((Number) result[0]).longValue());
            paymentRecordQuery.setParameter(2, null);
            paymentRecordQuery.setParameter(3, PaymentStatus.UNPAID.toString());
            paymentRecordQuery.executeUpdate();

            return new TaxRecordResponse(
                    ((Number) result[0]).longValue(),
                    ((Number) result[1]).longValue(),
                    (String) result[2],
                    (Double) result[3],
                    (Double) result[4],
                    DateUtil.localDateTimeToString(((Timestamp) result[5]).toLocalDateTime()),
                    "UNPAID"
            );
        }
        return null;
    }
}
