package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import com.skt.nova.billing.billcalculation.invoice.domain.UnpaidStatusCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoice_master")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(InvoiceMasterId.class)
public class InvoiceMasterJpaEntity {
    
    @Id
    @Column(name = "account_number", length = 10)
    private String accountNumber;
    
    @Id
    @Column(name = "billing_date")
    private LocalDate billingDate;
    
    @Id
    @Column(name = "service_management_number", length = 10)
    private String serviceManagementNumber;
    
    @Column(name = "total_billing_amount", precision = 15, scale = 2)
    private BigDecimal totalBillingAmount;
    
    @Column(name = "billing_amount", precision = 15, scale = 2)
    private BigDecimal billingAmount;
    
    @Column(name = "unpaid_amount", precision = 15, scale = 2)
    private BigDecimal unpaidAmount;
    
    @Column(name = "adjustment_amount", precision = 15, scale = 2)
    private BigDecimal adjustmentAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "unpaid_status_code", length = 1)
    private UnpaidStatusCode unpaidStatusCode;
    
    @OneToMany(mappedBy = "invoiceMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceDetailJpaEntity> invoiceDetails;

    @Column(name = "fully_paid_at_once")
    private boolean fullyPaidAtOnce;
} 