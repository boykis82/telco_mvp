package com.skt.nova.billing.billcalculation.payment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 수납 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    
    /**
     * 계정번호
     */
    private String accountNumber;
    
    /**
     * 서비스 관리 번호
     */
    private String serviceManagementNumber;
    
    /**
     * 청구일자
     */
    private LocalDate billingDate;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;
} 