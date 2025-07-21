package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.skt.nova.billing.billcalculation.invoice.infrastructure.QInvoiceMasterJpaEntity.invoiceMasterJpaEntity;

@Repository
@RequiredArgsConstructor
public class InvoiceMasterQueryRepository {
    
    private final JPAQueryFactory queryFactory;
    
    public List<InvoiceSummaryDto> findInvoiceSummaryByAccountNumber(String accountNumber) {
        return queryFactory
                .select(Projections.constructor(InvoiceSummaryDto.class,
                        invoiceMasterJpaEntity.accountNumber,
                        invoiceMasterJpaEntity.billingDate,
                        invoiceMasterJpaEntity.totalBillingAmount.sum(),
                        invoiceMasterJpaEntity.billingAmount.sum(),
                        invoiceMasterJpaEntity.unpaidAmount.sum()))
                .from(invoiceMasterJpaEntity)
                .where(invoiceMasterJpaEntity.accountNumber.eq(accountNumber))
                .groupBy(invoiceMasterJpaEntity.accountNumber, invoiceMasterJpaEntity.billingDate)
                .orderBy(invoiceMasterJpaEntity.billingDate.desc())
                .fetch();
    }
    
    public List<InvoiceMasterJpaEntity> findByAccountNumberAndBillingDateWithDetails(String accountNumber, LocalDate billingDate) {
        return queryFactory
                .selectFrom(invoiceMasterJpaEntity)
                .leftJoin(invoiceMasterJpaEntity.invoiceDetails)
                .fetchJoin()
                .where(
                        invoiceMasterJpaEntity.accountNumber.eq(accountNumber),
                        invoiceMasterJpaEntity.billingDate.eq(billingDate)
                )
                .fetch();
    }
    
    /**
     * 미납 청구 마스터 조회
     * @param accountNumber 계정번호
     * @return 미납 청구 마스터 목록 (billingDate 오름차순, serviceManagementNumber 오름차순)
     */
    public List<InvoiceMasterJpaEntity> findUnpaidInvoiceMastersByAccountNumber(String accountNumber) {
        return queryFactory
                .selectFrom(invoiceMasterJpaEntity)
                .leftJoin(invoiceMasterJpaEntity.invoiceDetails)
                .fetchJoin()
                .where(
                        invoiceMasterJpaEntity.accountNumber.eq(accountNumber),
                        invoiceMasterJpaEntity.unpaidAmount.gt(0),
                        invoiceMasterJpaEntity.invoiceDetails.any().unpaidAmount.ne(BigDecimal.ZERO)
                )
                .orderBy(
                        invoiceMasterJpaEntity.billingDate.asc(),
                        invoiceMasterJpaEntity.serviceManagementNumber.asc()
                )
                .fetch();
    }
} 