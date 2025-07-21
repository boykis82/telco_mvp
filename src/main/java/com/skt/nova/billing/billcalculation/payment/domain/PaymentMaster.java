package com.skt.nova.billing.billcalculation.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 수납 마스터 도메인 클래스 (Aggregate Root)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMaster {
    
    /**
     * 계정번호
     */
    private String accountNumber;
    
    /**
     * 서비스 관리 번호
     */
    private String serviceManagementNumber;
    
    /**
     * 수납 처리 일시
     */
    private LocalDateTime paymentDateTime;
    
    /**
     * 수납 구분 코드
     */
    private PaymentClassificationCode paymentClassificationCode;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;

    private LocalDate billingDate;
    
    /**
     * 수납 상세 목록 (1:n 관계)
     */
    @Builder.Default
    private List<PaymentDetail> paymentDetails = new ArrayList<>();
    
    /**
     * 수납 상세 추가
     */
    public void addPaymentDetail(PaymentDetail paymentDetail) {
        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }
        this.paymentDetails.add(paymentDetail);
    }
    
    /**
     * 수납 상세 목록 추가
     */
    public void addPaymentDetails(List<PaymentDetail> paymentDetails) {
        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }
        this.paymentDetails.addAll(paymentDetails);
    }
} 