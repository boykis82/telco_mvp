package com.skt.nova.billing.billcalculation.payment.api.dto;

import com.skt.nova.billing.billcalculation.payment.domain.PaymentClassificationCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 수납 내역 요약 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSummaryDto {
    
    /**
     * 계정번호
     */
    private String accountNumber;
    
    /**
     * 수납 처리 일시
     */
    private LocalDateTime paymentDateTime;
    
    /**
     * 수납 구분 코드
     */
    private PaymentClassificationCode paymentClassificationCode;
    
    /**
     * 수납금액 합계
     */
    private BigDecimal totalPaymentAmount;
} 