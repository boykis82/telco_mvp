package com.skt.nova.billing.billcalculation.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.skt.nova.billing.billcalculation.common.exception.BusinessException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetail {
    private String accountNumber;
    private String billingDate;
    private String serviceManagementNumber;
    private String revenueItemCode;
    private BigDecimal billingSequence;
    private BillingClassificationCode billingClassificationCode;
    private BigDecimal billingAmount;
    private BigDecimal unpaidAmount;

    public void validateAdjustment(BigDecimal adjustmentAmount) {
        // 3) 입력의 adjustmentRequestAmount가 음수이고 invoiceDetail의 unpaidAmount가 양수이면 두 값의 합이 음수이면 BusinessException
        if (adjustmentAmount.compareTo(BigDecimal.ZERO) < 0 && this.unpaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (this.unpaidAmount.add(adjustmentAmount).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(
                    "조정 금액(" + adjustmentAmount + ")이 미납 금액(" + this.unpaidAmount + ")보다 커서 미납액을 음수로 만들 수 없습니다."
                );
            }
        }
    }

    public void applyAdjustment(BigDecimal adjustmentAmount) {
        this.unpaidAmount = this.unpaidAmount.add(adjustmentAmount);
    }

    public void applyAdjustmentCancelation(BigDecimal adjustmentAmount) {
        if (this.billingClassificationCode == BillingClassificationCode.INVOICE) {
            this.unpaidAmount = this.unpaidAmount.add(adjustmentAmount);
        } else {
            this.billingAmount = this.billingAmount.subtract(adjustmentAmount);
        }
    }    

    public InvoiceDetail createAdjustmentDetail(BigDecimal adjustmentAmount) {
        return InvoiceDetail.builder()
                .accountNumber(this.accountNumber)
                .billingDate(this.billingDate)
                .serviceManagementNumber(this.serviceManagementNumber)
                .revenueItemCode(this.revenueItemCode)
                .billingSequence(this.billingSequence) // ToDo: 채번 로직 확인 필요
                .billingClassificationCode(BillingClassificationCode.AFTER_ADJUSTMENT)
                .billingAmount(adjustmentAmount)
                .unpaidAmount(BigDecimal.ZERO)
                .build();
    }
    
    /**
     * 수납 적용
     * @param paymentAmount 수납금액
     */
    public void applyPayment(BigDecimal paymentAmount) {
        this.unpaidAmount = this.unpaidAmount.subtract(paymentAmount);
    }
} 