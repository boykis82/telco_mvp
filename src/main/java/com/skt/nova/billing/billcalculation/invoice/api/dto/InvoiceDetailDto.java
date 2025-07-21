package com.skt.nova.billing.billcalculation.invoice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDto {
    private String accountNumber;
    private String billingDate;
    private String serviceManagementNumber;
    private String revenueItemCode;
    private BigDecimal billingSequence;
    private String billingClassificationCode;
    private BigDecimal billingAmount;
    private BigDecimal unpaidAmount;
} 