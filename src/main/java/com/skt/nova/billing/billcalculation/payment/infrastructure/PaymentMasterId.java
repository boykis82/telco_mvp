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
public class PaymentMasterId implements Serializable {
    
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
} 