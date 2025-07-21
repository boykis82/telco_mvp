package com.skt.nova.billing.billcalculation.invoice.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 수납 적용 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPaymentRequestDto {
    
    /**
     * 계정번호
     */
    private String accountNumber;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;
} 