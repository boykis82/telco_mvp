package com.skt.nova.billing.billcalculation.adjustment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adjustment {
    private String serviceManagementNumber;
    private LocalDate adjustmentDate;
    private String revenueItemCode;
    private Integer adjustmentSequence;
    private BigDecimal adjustmentRequestAmount;
    private AdjustmentType adjustmentType;
    private AdjustmentStatusCode adjustmentStatusCode;
    private LocalDateTime adjustmentRequestDateTime;
    private AdjustmentReasonCode adjustmentReasonCode;
    private String adjustmentReasonPhrase;
    private String adjustmentApproveRequestUserId;
    private String adjustmentApproveUserId;
    private LocalDateTime adjustmentApprovedDateTime;
    private String accountNumber;
    private LocalDate billingDate;
    private String adjustmentRequestId;
    private String vocId;

    public void approve(LocalDateTime adjustmentApprovedDateTime, String adjustmentApproveUserId) {
        this.adjustmentStatusCode = AdjustmentStatusCode.APPROVE;
        this.adjustmentApprovedDateTime = adjustmentApprovedDateTime;
        this.adjustmentApproveUserId = adjustmentApproveUserId;
    }

    public void cancel() {
        this.adjustmentStatusCode = AdjustmentStatusCode.CANCEL;
    }

    public void reject(LocalDateTime adjustmentRejectDateTime, String adjustmentRejectUserId) {
        this.adjustmentStatusCode = AdjustmentStatusCode.REJECT;
        this.adjustmentApprovedDateTime = adjustmentRejectDateTime;
        this.adjustmentApproveUserId = adjustmentRejectUserId;        
    }
} 