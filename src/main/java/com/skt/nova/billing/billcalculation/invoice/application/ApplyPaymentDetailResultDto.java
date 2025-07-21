package com.skt.nova.billing.billcalculation.invoice.application;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPaymentDetailResultDto {
        
    /**
     * 매출 항목 코드
     */
    private String revenueItemCode;

    private Long billingSequence;
    
    /**
     * 수납금액
     */
    private BigDecimal paymentAmount;
}
