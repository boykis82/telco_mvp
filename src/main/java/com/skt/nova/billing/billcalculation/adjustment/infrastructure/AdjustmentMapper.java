package com.skt.nova.billing.billcalculation.adjustment.infrastructure;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdjustmentMapper {

    public AdjustmentJpaEntity mapToJpaEntity(Adjustment domain) {
        if (domain == null) {
            return null;
        }

        AdjustmentId adjustmentId = new AdjustmentId(
                domain.getServiceManagementNumber(),
                domain.getAdjustmentDate(),
                domain.getRevenueItemCode(),
                domain.getAdjustmentSequence()
        );

        return AdjustmentJpaEntity.builder()
                .id(adjustmentId)
                .accountNumber(domain.getAccountNumber())
                .billingDate(domain.getBillingDate())
                .adjustmentRequestAmount(domain.getAdjustmentRequestAmount())
                .adjustmentType(domain.getAdjustmentType())
                .adjustmentStatusCode(domain.getAdjustmentStatusCode())
                .adjustmentRequestDateTime(domain.getAdjustmentRequestDateTime())
                .adjustmentReasonCode(domain.getAdjustmentReasonCode())
                .adjustmentReasonPhrase(domain.getAdjustmentReasonPhrase())
                .adjustmentApproveRequestUserId(domain.getAdjustmentApproveRequestUserId())
                .adjustmentApproveUserId(domain.getAdjustmentApproveUserId())
                .adjustmentApprovedDateTime(domain.getAdjustmentApprovedDateTime())
                .adjustmentRequestId(domain.getAdjustmentRequestId())
                .build();
    }

    public List<AdjustmentJpaEntity> mapToJpaEntityList(List<Adjustment> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream().map(this::mapToJpaEntity).collect(Collectors.toList());
    }

    public Adjustment mapToDomainEntity(AdjustmentJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Adjustment.builder()
                .serviceManagementNumber(entity.getId().getServiceManagementNumber())
                .adjustmentDate(entity.getId().getAdjustmentDate())
                .revenueItemCode(entity.getId().getRevenueItemCode())
                .adjustmentSequence(entity.getId().getAdjustmentSequence())
                .accountNumber(entity.getAccountNumber())
                .billingDate(entity.getBillingDate())
                .adjustmentRequestAmount(entity.getAdjustmentRequestAmount())
                .adjustmentType(entity.getAdjustmentType())
                .adjustmentStatusCode(entity.getAdjustmentStatusCode())
                .adjustmentRequestDateTime(entity.getAdjustmentRequestDateTime())
                .adjustmentReasonCode(entity.getAdjustmentReasonCode())
                .adjustmentReasonPhrase(entity.getAdjustmentReasonPhrase())
                .adjustmentApproveRequestUserId(entity.getAdjustmentApproveRequestUserId())
                .adjustmentApproveUserId(entity.getAdjustmentApproveUserId())
                .adjustmentApprovedDateTime(entity.getAdjustmentApprovedDateTime())
                .adjustmentRequestId(entity.getAdjustmentRequestId())
                .build();
    }
} 