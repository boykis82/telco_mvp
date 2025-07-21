package com.skt.nova.billing.billcalculation.invoice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillingClassificationCode {
    INVOICE("000", "청구분"),
    AFTER_ADJUSTMENT("006", "후조정"),
    BEFORE_ADJUSTMENT("001", "전조정"),
    ADJUSTMENT_OVERPAYMENT("999", "조정과납");

    private final String code;
    private final String description;

    public boolean isAdjustmentClassficationCode() {
        return this == AFTER_ADJUSTMENT || this == BEFORE_ADJUSTMENT;
    }

    public boolean isInvoiceClassficationCode() {
        return this == INVOICE;
    }    
} 