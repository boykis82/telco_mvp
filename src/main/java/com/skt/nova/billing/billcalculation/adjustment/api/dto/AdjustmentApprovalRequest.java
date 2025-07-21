package com.skt.nova.billing.billcalculation.adjustment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentApprovalRequest {
    private String adjustmentRequestId;
    private String adjustmentApproveUserId;
    private LocalDateTime adjustmentApprovedDateTime;
} 