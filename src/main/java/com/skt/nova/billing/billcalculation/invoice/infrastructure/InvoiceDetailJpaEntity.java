package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(InvoiceDetailId.class)
public class InvoiceDetailJpaEntity {
    
    @Id
    @Column(name = "account_number", length = 10)
    private String accountNumber;
    
    @Id
    @Column(name = "billing_date")
    private String billingDate;
    
    @Id
    @Column(name = "service_management_number", length = 10)
    private String serviceManagementNumber;
    
    @Id
    @Column(name = "revenue_item_code", length = 7)
    private String revenueItemCode;
    
    @Id
    @Column(name = "billing_sequence", precision = 3, scale = 0)
    private BigDecimal billingSequence;
    
    @Id
    @Column(name = "billing_classification_code", length = 3)
    private String billingClassificationCode;
    
    @Column(name = "billing_amount", precision = 15, scale = 2)
    private BigDecimal billingAmount;
    
    @Column(name = "unpaid_amount", precision = 15, scale = 2)
    private BigDecimal unpaidAmount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false),
        @JoinColumn(name = "billing_date", referencedColumnName = "billing_date", insertable = false, updatable = false),
        @JoinColumn(name = "service_management_number", referencedColumnName = "service_management_number", insertable = false, updatable = false)
    })
    private InvoiceMasterJpaEntity invoiceMaster;
} 