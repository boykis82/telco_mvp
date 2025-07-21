package com.skt.nova.billing.billcalculation.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.skt.nova.billing.billcalculation.common.exception.BusinessException;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMaster {
    private String accountNumber;
    private LocalDate billingDate;
    private String serviceManagementNumber;
    private BigDecimal totalBillingAmount;
    private BigDecimal billingAmount;
    private BigDecimal unpaidAmount;
    private BigDecimal adjustmentAmount;
    private UnpaidStatusCode unpaidStatusCode;
    private List<InvoiceDetail> invoiceDetails;
    private boolean fullyPaidAtOnce;

    public void applyAdjustment(List<AdjustmentItem> items) {
        validateTotalAmount(items);

        List<InvoiceDetail> newAdjustmentDetails = new ArrayList<>();
        for (AdjustmentItem item : items) {
            InvoiceDetail targetDetail = findInvoiceDetail(item.getRevenueItemCode());

            targetDetail.validateAdjustment(item.getAdjustmentRequestAmount());
            targetDetail.applyAdjustment(item.getAdjustmentRequestAmount());

            InvoiceDetail adjustmentDetail = targetDetail.createAdjustmentDetail(item.getAdjustmentRequestAmount());
            newAdjustmentDetails.add(adjustmentDetail);
        }

        this.invoiceDetails.addAll(newAdjustmentDetails);
        this.updateMasterAmounts();
    }
    
    public void cancelAdjustment(List<AdjustmentItem> items) {
        for (AdjustmentItem item : items) {
            // INVOICE인 대상의 unpaidAmount에 adjustmentRequestAmount를 더함
            InvoiceDetail invoiceDetail = findInvoiceDetail(item.getRevenueItemCode());
            invoiceDetail.applyAdjustmentCancelation(item.getAdjustmentRequestAmount());
            
            // AFTER_ADJUSTMENT인 대상의 billingAmount에서 adjustmentRequestAmount를 뺌
            InvoiceDetail adjustmentDetail = findAdjustmentDetail(item.getRevenueItemCode());
            adjustmentDetail.applyAdjustmentCancelation(item.getAdjustmentRequestAmount());
        }
        
        this.updateMasterAmounts();
    }

    private void validateTotalAmount(List<AdjustmentItem> items) {
        BigDecimal totalAdjustmentAmount = items.stream()
                .map(AdjustmentItem::getAdjustmentRequestAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.unpaidAmount.add(totalAdjustmentAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("총 조정 금액이 미납 금액보다 커서 전체 미납액을 음수로 만들 수 없습니다.");
        }
    }

    private InvoiceDetail findInvoiceDetail(String revenueItemCode) {
        return findInvoiceDetailByClassification(revenueItemCode, BillingClassificationCode.INVOICE,
                "조정 대상 매출 항목(" + revenueItemCode + ")이 존재하지 않습니다.");
    }

    private InvoiceDetail findAdjustmentDetail(String revenueItemCode) {
        return findInvoiceDetailByClassification(revenueItemCode, BillingClassificationCode.AFTER_ADJUSTMENT,
                "조정 취소 대상 매출 항목(" + revenueItemCode + ")이 존재하지 않습니다.");
    }

    /**
     * 매출 항목 코드와 청구 구분 코드로 InvoiceDetail을 찾는 공통 메서드로 추출
     */
    private InvoiceDetail findInvoiceDetailByClassification(String revenueItemCode, BillingClassificationCode classificationCode, String notFoundMessage) {
        return this.invoiceDetails.stream()
                .filter(d -> Objects.equals(d.getRevenueItemCode(), revenueItemCode)
                        && d.getBillingClassificationCode() == classificationCode)
                .findFirst()
                .orElseThrow(() -> new BusinessException(notFoundMessage));
    }

    private void updateMasterAmounts() {
        this.adjustmentAmount = this.invoiceDetails.stream()
                .filter(d -> d.getBillingClassificationCode().isAdjustmentClassficationCode())
                .map(InvoiceDetail::getBillingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.unpaidAmount = this.invoiceDetails.stream()
                .map(InvoiceDetail::getUnpaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.unpaidAmount.compareTo(BigDecimal.ZERO) == 0 && this.unpaidStatusCode == UnpaidStatusCode.OPEN) {
            this.unpaidStatusCode = UnpaidStatusCode.CLOSE;
        }
    }
    
    /**
     * 수납 적용
     * @param paymentAmount 수납금액
     * @return 수납 적용 결과 목록
     */
    public List<ApplyPaymentResult> applyPayment(BigDecimal paymentAmount) {
        List<ApplyPaymentResult> results = new ArrayList<>();
        
        // 첫 전체 수납
        if (isFirstFullPaymentApplied(paymentAmount)) {
            this.fullyPaidAtOnce = true;
            results = createFullPaymentResults();
        } else {
            // 부분 수납
            this.fullyPaidAtOnce = false;
            results = createPartialPaymentResults(paymentAmount);
        }
        
        // 마스터 금액 업데이트
        this.updateMasterAmounts();
        
        return results;
    }

    private boolean isFirstFullPaymentApplied(BigDecimal paymentAmount) {
        return paymentAmount.compareTo(this.unpaidAmount) >= 0 && 
            this.billingAmount.compareTo(this.unpaidAmount) == 0 &&
            this.invoiceDetails.stream()
                .allMatch(detail -> detail.getBillingClassificationCode().isInvoiceClassficationCode());
    }
    
    /**
     * 전체 수납 결과 생성
     */
    private List<ApplyPaymentResult> createFullPaymentResults() {
        return this.invoiceDetails.stream()
                .filter(detail -> detail.getBillingClassificationCode().isInvoiceClassficationCode())
                .map(detail -> ApplyPaymentResult.builder()
                        .revenueItemCode(detail.getRevenueItemCode())
                        .paymentAmount(detail.getBillingAmount())
                        .build())
                .collect(Collectors.toList());
    }
    
    /**
     * 부분 수납 결과 생성
     */
    private List<ApplyPaymentResult> createPartialPaymentResults(BigDecimal paymentAmount) {
        List<ApplyPaymentResult> results = new ArrayList<>();
        BigDecimal remainingAmount = paymentAmount;
        
        // INVOICE인 대상의 unpaidAmount를 차례대로 차감
        List<InvoiceDetail> deductionTargetInvoiceDetails = this.invoiceDetails.stream()
                .filter(detail -> detail.getBillingClassificationCode().isInvoiceClassficationCode())
                .sorted(Comparator
                        .comparing((InvoiceDetail detail) -> detail.getUnpaidAmount().compareTo(BigDecimal.ZERO) < 0) // 음수인 것부터
                        .thenComparing(InvoiceDetail::getRevenueItemCode)
                        .thenComparing(InvoiceDetail::getBillingSequence)) // 그 후 revenueItemCode 순, 그 후 billingSequence 순                        
                .collect(Collectors.toList());
        
        for (InvoiceDetail detail : deductionTargetInvoiceDetails) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            BigDecimal detailUnpaidAmount = detail.getUnpaidAmount();
            if (detailUnpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                continue; // 이미 완납된 항목은 스킵
            }
            
            BigDecimal appliedAmount = remainingAmount.compareTo(detailUnpaidAmount) >= 0 
                    ? detailUnpaidAmount 
                    : remainingAmount;
            
            // InvoiceDetail의 unpaidAmount 차감
            detail.applyPayment(appliedAmount);
            
            // 결과 추가
            results.add(ApplyPaymentResult.builder()
                    .revenueItemCode(detail.getRevenueItemCode())
                    .paymentAmount(appliedAmount)
                    .build()
            );
            
            remainingAmount = remainingAmount.subtract(appliedAmount);
        }
        
        return results;
    }
} 