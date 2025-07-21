package com.skt.nova.billing.billcalculation.adjustment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdjustmentType {
    BEFORE_ADJUSTMENT("B", "전조정"),
    AFTER_ADJUSTMENT("A", "후조정");

    private final String code;
    private final String description;
} 