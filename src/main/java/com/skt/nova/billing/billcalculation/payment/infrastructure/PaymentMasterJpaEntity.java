package com.skt.nova.billing.billcalculation.payment.infrastructure;

import com.skt.nova.billing.billcalculation.payment.domain.PaymentClassificationCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_master")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PaymentMasterId.class)
public class PaymentMasterJpaEntity {
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_classification_code", length = 1)
    private PaymentClassificationCode paymentClassificationCode;
    
    @Column(name = "payment_amount", precision = 15, scale = 2)
    private BigDecimal paymentAmount;
    
    /**
     * 수납 상세 목록 (1:n 관계)
     */
    @OneToMany(mappedBy = "paymentMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<PaymentDetailJpaEntity> paymentDetails = new ArrayList<>();
    
    /**
     * 수납 상세 추가
     */
    public void addPaymentDetail(PaymentDetailJpaEntity paymentDetail) {
        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }
        paymentDetail.setPaymentMaster(this);
        this.paymentDetails.add(paymentDetail);
    }
    
    /**
     * 수납 상세 목록 추가
     */
    public void addPaymentDetails(List<PaymentDetailJpaEntity> paymentDetails) {
        if (this.paymentDetails == null) {
            this.paymentDetails = new ArrayList<>();
        }
        paymentDetails.forEach(detail -> detail.setPaymentMaster(this));
        this.paymentDetails.addAll(paymentDetails);
    }

} 