package com.skt.nova.billing.billcalculation.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 조정 항목을 나타내는 도메인 값 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentItem {
    private String revenueItemCode;
    private BigDecimal adjustmentRequestAmount;
} 