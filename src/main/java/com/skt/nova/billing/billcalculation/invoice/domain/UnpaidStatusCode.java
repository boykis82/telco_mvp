package com.skt.nova.billing.billcalculation.invoice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnpaidStatusCode {
    OPEN("O", "미납"),
    CLOSE("C", "완납");

    private final String code;
    private final String description;
} 