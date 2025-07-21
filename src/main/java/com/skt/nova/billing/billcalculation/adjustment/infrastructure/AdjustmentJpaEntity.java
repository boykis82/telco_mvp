package com.skt.nova.billing.billcalculation.adjustment.infrastructure;

import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentReasonCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADJUSTMENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentJpaEntity {
    @EmbeddedId
    private AdjustmentId id;

    @Column(name = "ADJUSTMENT_REQUEST_AMOUNT", precision = 15, scale = 2, nullable = false)
    private BigDecimal adjustmentRequestAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ADJUSTMENT_TYPE", length = 1, nullable = false)
    private AdjustmentType adjustmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ADJUSTMENT_STATUS_CODE", length = 2, nullable = false)
    private AdjustmentStatusCode adjustmentStatusCode;

    @Column(name = "ADJUSTMENT_REQUEST_DATETIME", nullable = false)
    private LocalDateTime adjustmentRequestDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ADJUSTMENT_REASON_CODE", length = 4)
    private AdjustmentReasonCode adjustmentReasonCode;

    @Column(name = "ADJUSTMENT_REASON_PHRASE", length = 100)
    private String adjustmentReasonPhrase;

    @Column(name = "ADJUSTMENT_APPROVE_REQUEST_USER_ID", length = 10)
    private String adjustmentApproveRequestUserId;

    @Column(name = "ADJUSTMENT_APPROVE_USER_ID", length = 10)
    private String adjustmentApproveUserId;

    @Column(name = "ADJUSTMENT_APPROVED_DATETIME")
    private LocalDateTime adjustmentApprovedDateTime;

    @Column(name = "ACCOUNT_NUMBER", length = 10)
    private String accountNumber;    

    @Column(name = "BILLING_DATE")
    private LocalDate billingDate;        

    @Column(name = "ADJUSTMENT_REQUEST_ID", length = 10)
    private String adjustmentRequestId;
} 