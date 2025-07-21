package com.skt.nova.billing.billcalculation.adjustment.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AfterAdjustmentResponse {
    private final String status;
    private final String approvalRequestId;
} 