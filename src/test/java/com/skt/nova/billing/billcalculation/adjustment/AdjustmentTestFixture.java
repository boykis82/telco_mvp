package com.skt.nova.billing.billcalculation.adjustment;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentReasonCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AdjustmentTestFixture {
    
    public static Adjustment createAdjustmentWithStatus(AdjustmentStatusCode status) {
        return Adjustment.builder()
                .serviceManagementNumber("1234567890")
                .adjustmentDate(LocalDate.of(2024, 1, 31))
                .revenueItemCode("REV001")
                .adjustmentSequence(1)
                .adjustmentRequestAmount(new BigDecimal("-1000"))
                .adjustmentType(AdjustmentType.AFTER_ADJUSTMENT)
                .adjustmentStatusCode(status)
                .adjustmentRequestDateTime(LocalDateTime.of(2024, 1, 31, 10, 0, 0))
                .adjustmentReasonCode(AdjustmentReasonCode.BILLING_ERROR)
                .adjustmentReasonPhrase("고객 요청")
                .adjustmentApproveRequestUserId("REQUEST_USER")
                .adjustmentApproveUserId("APPROVE_USER")
                .adjustmentApprovedDateTime(LocalDateTime.of(2024, 1, 31, 11, 0, 0))
                .accountNumber("9876543210")
                .billingDate(LocalDate.of(2024, 1, 31))
                .adjustmentRequestId("ADJ_REQ_001")
                .vocId("VOC_001")
                .build();
    }
    
    public static Adjustment createAdjustmentWithCustomValues(
            String adjustmentRequestId, 
            AdjustmentStatusCode status,
            String approveUserId,
            LocalDateTime approvedDateTime) {
        return Adjustment.builder()
                .serviceManagementNumber("1234567890")
                .adjustmentDate(LocalDate.of(2024, 1, 31))
                .revenueItemCode("REV001")
                .adjustmentSequence(1)
                .adjustmentRequestAmount(new BigDecimal("-1000"))
                .adjustmentType(AdjustmentType.AFTER_ADJUSTMENT)
                .adjustmentStatusCode(status)
                .adjustmentRequestDateTime(LocalDateTime.of(2024, 1, 31, 10, 0, 0))
                .adjustmentReasonCode(AdjustmentReasonCode.BILLING_ERROR)
                .adjustmentReasonPhrase("고객 요청")
                .adjustmentApproveRequestUserId("REQUEST_USER")
                .adjustmentApproveUserId(approveUserId)
                .adjustmentApprovedDateTime(approvedDateTime)
                .accountNumber("9876543210")
                .billingDate(LocalDate.of(2024, 1, 31))
                .adjustmentRequestId(adjustmentRequestId)
                .vocId("VOC_001")
                .build();
    }
    
    public static List<Adjustment> createMultipleAdjustments(String adjustmentRequestId, AdjustmentStatusCode status) {
        return Arrays.asList(
                Adjustment.builder()
                        .serviceManagementNumber("1234567890")
                        .adjustmentDate(LocalDate.of(2024, 1, 31))
                        .revenueItemCode("REV001")
                        .adjustmentSequence(1)
                        .adjustmentRequestAmount(new BigDecimal("-1000"))
                        .adjustmentType(AdjustmentType.AFTER_ADJUSTMENT)
                        .adjustmentStatusCode(status)
                        .adjustmentRequestDateTime(LocalDateTime.of(2024, 1, 31, 10, 0, 0))
                        .adjustmentReasonCode(AdjustmentReasonCode.BILLING_ERROR)
                        .adjustmentReasonPhrase("고객 요청")
                        .adjustmentApproveRequestUserId("REQUEST_USER")
                        .adjustmentApproveUserId("APPROVE_USER")
                        .adjustmentApprovedDateTime(LocalDateTime.of(2024, 1, 31, 11, 0, 0))
                        .accountNumber("9876543210")
                        .billingDate(LocalDate.of(2024, 1, 31))
                        .adjustmentRequestId(adjustmentRequestId)
                        .vocId("VOC_001")
                        .build(),
                Adjustment.builder()
                        .serviceManagementNumber("1234567890")
                        .adjustmentDate(LocalDate.of(2024, 1, 31))
                        .revenueItemCode("REV002")
                        .adjustmentSequence(2)
                        .adjustmentRequestAmount(new BigDecimal("-500"))
                        .adjustmentType(AdjustmentType.AFTER_ADJUSTMENT)
                        .adjustmentStatusCode(status)
                        .adjustmentRequestDateTime(LocalDateTime.of(2024, 1, 31, 10, 0, 0))
                        .adjustmentReasonCode(AdjustmentReasonCode.BILLING_ERROR)
                        .adjustmentReasonPhrase("고객 요청")
                        .adjustmentApproveRequestUserId("REQUEST_USER")
                        .adjustmentApproveUserId("APPROVE_USER")
                        .adjustmentApprovedDateTime(LocalDateTime.of(2024, 1, 31, 11, 0, 0))
                        .accountNumber("9876543210")
                        .billingDate(LocalDate.of(2024, 1, 31))
                        .adjustmentRequestId(adjustmentRequestId)
                        .vocId("VOC_001")
                        .build()
        );
    }
    
    public static List<Adjustment> createAdjustmentsWithMixedStatus(String adjustmentRequestId) {
        return Arrays.asList(
                createAdjustmentWithCustomValues(adjustmentRequestId, AdjustmentStatusCode.APPROVE_REQUEST, "APPROVE_USER", LocalDateTime.of(2024, 1, 31, 11, 0, 0)),
                createAdjustmentWithCustomValues(adjustmentRequestId, AdjustmentStatusCode.APPROVE, "APPROVE_USER", LocalDateTime.of(2024, 1, 31, 11, 0, 0))
        );
    }
} 