package com.skt.nova.billing.billcalculation.invoice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSummaryDto {
    private String accountNumber;
    private LocalDate billingDate;
    private BigDecimal totalBillingAmount;
    private BigDecimal billingAmount;
    private BigDecimal unpaidAmount;
}