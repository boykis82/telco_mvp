package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMasterId implements Serializable {
    private String accountNumber;
    private LocalDate billingDate;
    private String serviceManagementNumber;
} 