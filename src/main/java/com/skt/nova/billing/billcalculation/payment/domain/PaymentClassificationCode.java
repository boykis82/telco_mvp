package com.skt.nova.billing.billcalculation.payment.domain;

import lombok.Getter;

/**
 * 수납 구분 코드
 */
@Getter
public enum PaymentClassificationCode {
    
    /**
     * 수납
     */
    PAYMENT("1","수납"),
    
    /**
     * 환불
     */
    REFUND("5","환불");
    
    private final String code;
    private final String description;
    
    PaymentClassificationCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 코드값으로 enum 찾기
     */
    public static PaymentClassificationCode fromCode(String code) {
        for (PaymentClassificationCode classificationCode : values()) {
            if (classificationCode.code.equals(code)) {
                return classificationCode;
            }
        }
        throw new IllegalArgumentException("Unknown payment classification code: " + code);
    }
} 