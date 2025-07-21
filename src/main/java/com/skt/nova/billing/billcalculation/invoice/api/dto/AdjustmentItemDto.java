package com.skt.nova.billing.billcalculation.invoice.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AdjustmentItemDto {
    private String revenueItemCode;
    private BigDecimal adjustmentRequestAmount;
} 