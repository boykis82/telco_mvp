package com.skt.nova.billing.billcalculation.payment.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailId implements Serializable {
    
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
} 