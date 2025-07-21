package com.skt.nova.billing.billcalculation.payment.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PaymentDetailId.class)
public class PaymentDetailJpaEntity {
    
    @Id
    @Column(name = "account_number", length = 10)
    private String accountNumber;
    
    @Id
    @Column(name = "service_management_number", length = 10)
    private String serviceManagementNumber;
    
    @Id
    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;

    @Id
    @Column(name = "billing_date")
    private LocalDate billingDate;    
    
    @Id
    @Column(name = "revenue_item_code", length = 7)
    private String revenueItemCode;
    
    @Id
    @Column(name = "billing_sequence", precision = 3)
    private Long billingSequence;
    
    @Column(name = "payment_amount", precision = 15, scale = 2)
    private BigDecimal paymentAmount;
    
    /**
     * 수납 마스터 참조 (n:1 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false),
        @JoinColumn(name = "service_management_number", referencedColumnName = "service_management_number", insertable = false, updatable = false),
        @JoinColumn(name = "payment_date_time", referencedColumnName = "payment_date_time", insertable = false, updatable = false),
        @JoinColumn(name = "billing_date", referencedColumnName = "billing_date", insertable = false, updatable = false)
    })
    private PaymentMasterJpaEntity paymentMaster;
} 