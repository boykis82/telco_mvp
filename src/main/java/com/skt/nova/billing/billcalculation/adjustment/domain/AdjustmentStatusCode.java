package com.skt.nova.billing.billcalculation.adjustment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdjustmentStatusCode {
    APPROVE("AP", "승인"),
    COMPLETED("BL", "완료"),
    APPROVE_REQUEST("PD", "승인요청"),
    CANCEL("CN", "취소"),
    REJECT("RX", "반려");

    private final String code;
    private final String description;
} 