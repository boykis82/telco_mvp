package com.skt.nova.billing.billcalculation.adjustment.api.dto;

import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentReasonCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentType;
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
public class AdjustmentDto {
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
} 