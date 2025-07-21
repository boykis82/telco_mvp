package com.skt.nova.billing.billcalculation.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 수납 상세 도메인 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    
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

    private LocalDate billingDate;
    
    /**
     * 수납된 매출 항목 코드
     */
    private String revenueItemCode;
    
    /**
     * 수납된 매출 항목 코드의 순번
     */
    private Long billingSequence;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;
} 