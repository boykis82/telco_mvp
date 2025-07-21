package com.skt.nova.billing.billcalculation.invoice.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApplyAdjustmentRequestDto {
    private String adjustmentRequestId;
    private String serviceManagementNumber;
    private String accountNumber;
    private String billingDate;
    private List<AdjustmentItemDto> items;
} 