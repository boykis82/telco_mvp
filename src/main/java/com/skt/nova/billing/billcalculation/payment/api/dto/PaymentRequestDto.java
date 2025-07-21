package com.skt.nova.billing.billcalculation.payment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 수납 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    
    /**
     * 계정번호
     */
    private String accountNumber;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;
    
    /**
     * 수납일시
     */
    private LocalDateTime paymentDateTime;
} 