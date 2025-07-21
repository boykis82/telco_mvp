package com.skt.nova.billing.billcalculation.invoice.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyPaymentResult {
    private final String revenueItemCode;
    private final BigDecimal paymentAmount;
}
