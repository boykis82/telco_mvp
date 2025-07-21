package com.skt.nova.billing.billcalculation.invoice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMasterDto {
    private String accountNumber;
    private LocalDate billingDate;
    private String serviceManagementNumber;
    private BigDecimal totalBillingAmount;
    private BigDecimal billingAmount;
    private BigDecimal unpaidAmount;
    private BigDecimal adjustmentAmount;
    private String unpaidStatusCode;
    private List<InvoiceDetailDto> invoiceDetails;
} 