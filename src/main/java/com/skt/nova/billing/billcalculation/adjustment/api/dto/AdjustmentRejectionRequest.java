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
public class AdjustmentRejectionRequest {
    private String adjustmentRequestId;
    private String adjustmentRejectUserId;
    private LocalDateTime adjustmentRejectedDateTime;
} 