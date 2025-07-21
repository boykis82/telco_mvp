package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailId implements Serializable {
    private String accountNumber;
    private String billingDate;
    private String serviceManagementNumber;
    private String revenueItemCode;
    private BigDecimal billingSequence;
    private String billingClassificationCode;
} 