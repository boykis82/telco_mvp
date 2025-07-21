package com.skt.nova.billing.billcalculation.invoice.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 수납 결과 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPaymentResultDto {
    
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
     * 한방에 수납 여부
     */
    private boolean fullyPaidAtOnce;

    private BigDecimal paymentAmount;

    private List<ApplyPaymentDetailResultDto> applyPaymentDetailResults;
} 