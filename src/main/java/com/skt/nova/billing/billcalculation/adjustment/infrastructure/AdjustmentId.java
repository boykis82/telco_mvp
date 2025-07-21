package com.skt.nova.billing.billcalculation.adjustment.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentId implements Serializable {
    @Column(name = "SERVICE_MANAGEMENT_NUMBER", precision = 10, nullable = false)
    private String serviceManagementNumber;

    @Column(name = "ADJUSTMENT_DATE", nullable = false)
    private LocalDate adjustmentDate;

    @Column(name = "REVENUE_ITEM_CODE", length = 7, nullable = false)
    private String revenueItemCode;

    @Column(name = "ADJUSTMENT_SEQUENCE", precision = 3, nullable = false)
    private Integer adjustmentSequence;
} 