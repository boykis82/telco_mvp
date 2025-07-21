package com.skt.nova.billing.billcalculation.adjustment.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdjustmentItem {
    private String revenueItemCode;
    private Long adjustmentRequestAmount;
} 