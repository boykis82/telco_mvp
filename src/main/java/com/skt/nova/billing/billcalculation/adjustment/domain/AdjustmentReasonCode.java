package com.skt.nova.billing.billcalculation.adjustment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdjustmentReasonCode {
    BILLING_ERROR("1000", "과금 오류"),
    ETC("9999", "기타");

    private final String code;
    private final String description;
} 