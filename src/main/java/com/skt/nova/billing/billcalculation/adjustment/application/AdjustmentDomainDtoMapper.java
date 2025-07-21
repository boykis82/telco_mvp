package com.skt.nova.billing.billcalculation.adjustment.application;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentDto;
import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;

@Component
public class AdjustmentDomainDtoMapper {
    public AdjustmentDto mapToDto(Adjustment adjustment) {
        return AdjustmentDto.builder()
                .serviceManagementNumber(adjustment.getServiceManagementNumber())
                .adjustmentDate(adjustment.getAdjustmentDate())
                .revenueItemCode(adjustment.getRevenueItemCode())
                .adjustmentSequence(adjustment.getAdjustmentSequence())
                .adjustmentRequestAmount(adjustment.getAdjustmentRequestAmount())
                .adjustmentType(adjustment.getAdjustmentType())
                .adjustmentStatusCode(adjustment.getAdjustmentStatusCode())
                .adjustmentRequestDateTime(adjustment.getAdjustmentRequestDateTime())
                .adjustmentReasonCode(adjustment.getAdjustmentReasonCode())
                .adjustmentReasonPhrase(adjustment.getAdjustmentReasonPhrase())
                .adjustmentApproveRequestUserId(adjustment.getAdjustmentApproveRequestUserId())
                .adjustmentApproveUserId(adjustment.getAdjustmentApproveUserId())
                .adjustmentApprovedDateTime(adjustment.getAdjustmentApprovedDateTime())
                .accountNumber(adjustment.getAccountNumber())
                .billingDate(adjustment.getBillingDate())
                .adjustmentRequestId(adjustment.getAdjustmentRequestId())
                .build();
    }
}
