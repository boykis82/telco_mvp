package com.skt.nova.billing.billcalculation.payment.infrastructure;

import com.skt.nova.billing.billcalculation.payment.domain.PaymentDetail;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    /**
     * PaymentMaster 도메인을 JPA Entity로 변환
     */
    public PaymentMasterJpaEntity mapToJpaEntity(PaymentMaster domain) {
        if (domain == null) {
            return null;
        }

        PaymentMasterId paymentMasterId = new PaymentMasterId(
                domain.getAccountNumber(),
                domain.getServiceManagementNumber(),
                domain.getPaymentDateTime(),
                domain.getBillingDate()
        );

        PaymentMasterJpaEntity entity = PaymentMasterJpaEntity.builder()
                .accountNumber(paymentMasterId.getAccountNumber())
                .serviceManagementNumber(paymentMasterId.getServiceManagementNumber())
                .paymentDateTime(paymentMasterId.getPaymentDateTime())
                .billingDate(domain.getBillingDate())             
                .paymentClassificationCode(domain.getPaymentClassificationCode())
                .paymentAmount(domain.getPaymentAmount())
                .build();
        
        // PaymentDetail 목록도 함께 변환
        if (domain.getPaymentDetails() != null && !domain.getPaymentDetails().isEmpty()) {
            List<PaymentDetailJpaEntity> detailEntities = domain.getPaymentDetails().stream()
                    .map(this::mapToJpaEntity)
                    .collect(Collectors.toList());
            entity.addPaymentDetails(detailEntities);
        }
        
        return entity;
    }

    /**
     * PaymentMaster 도메인 리스트를 JPA Entity 리스트로 변환
     */
    public List<PaymentMasterJpaEntity> mapToPaymentMasterJpaEntityList(List<PaymentMaster> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream().map(this::mapToJpaEntity).collect(Collectors.toList());
    }

    /**
     * PaymentMaster JPA Entity를 도메인으로 변환
     */
    public PaymentMaster mapToDomainEntity(PaymentMasterJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        PaymentMaster domain = PaymentMaster.builder()
                .accountNumber(entity.getAccountNumber())
                .serviceManagementNumber(entity.getServiceManagementNumber())
                .paymentDateTime(entity.getPaymentDateTime())
                .billingDate(entity.getBillingDate())
                .paymentClassificationCode(entity.getPaymentClassificationCode())
                .paymentAmount(entity.getPaymentAmount())
                .build();
        
        // PaymentDetail 목록도 함께 변환
        if (entity.getPaymentDetails() != null && !entity.getPaymentDetails().isEmpty()) {
            List<PaymentDetail> detailDomains = entity.getPaymentDetails().stream()
                    .map(this::mapToDomainEntity)
                    .collect(Collectors.toList());
            domain.addPaymentDetails(detailDomains);
        }
        
        return domain;
    }

    /**
     * PaymentDetail 도메인을 JPA Entity로 변환
     */
    public PaymentDetailJpaEntity mapToJpaEntity(PaymentDetail domain) {
        if (domain == null) {
            return null;
        }

        PaymentDetailId paymentDetailId = new PaymentDetailId(
                domain.getAccountNumber(),
                domain.getServiceManagementNumber(),
                domain.getPaymentDateTime(),
                domain.getBillingDate(),
                domain.getRevenueItemCode(),
                domain.getBillingSequence()
        );

        return PaymentDetailJpaEntity.builder()
                .accountNumber(paymentDetailId.getAccountNumber())
                .serviceManagementNumber(paymentDetailId.getServiceManagementNumber())
                .paymentDateTime(paymentDetailId.getPaymentDateTime())
                .billingDate(paymentDetailId.getBillingDate())
                .revenueItemCode(paymentDetailId.getRevenueItemCode())
                .billingSequence(paymentDetailId.getBillingSequence())
                .paymentAmount(domain.getPaymentAmount())
                .build();
    }

    /**
     * PaymentDetail 도메인 리스트를 JPA Entity 리스트로 변환
     */
    public List<PaymentDetailJpaEntity> mapToPaymentDetailJpaEntityList(List<PaymentDetail> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream().map(this::mapToJpaEntity).collect(Collectors.toList());
    }

    /**
     * PaymentDetail JPA Entity를 도메인으로 변환
     */
    public PaymentDetail mapToDomainEntity(PaymentDetailJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return PaymentDetail.builder()
                .accountNumber(entity.getAccountNumber())
                .serviceManagementNumber(entity.getServiceManagementNumber())
                .paymentDateTime(entity.getPaymentDateTime())
                .revenueItemCode(entity.getRevenueItemCode())
                .billingSequence(entity.getBillingSequence())
                .paymentAmount(entity.getPaymentAmount())
                .build();
    }
} 