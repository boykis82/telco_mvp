package com.skt.nova.billing.billcalculation.invoice.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.domain.BillingClassificationCode;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceDetail;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.domain.UnpaidStatusCode;

public class InvoiceTestFixture {
    public static List<InvoiceSummaryDto> createMockSummaries(String accountNumber) {
        return List.of(
                InvoiceSummaryDto.builder()
                        .accountNumber(accountNumber)
                        .billingDate(LocalDate.of(2024, 1, 31))
                        .totalBillingAmount(new BigDecimal("100000"))
                        .billingAmount(new BigDecimal("95000"))
                        .unpaidAmount(new BigDecimal("5000"))
                        .build(),
                InvoiceSummaryDto.builder()
                        .accountNumber(accountNumber)
                        .billingDate(LocalDate.of(2024, 2, 28))
                        .totalBillingAmount(new BigDecimal("100000"))
                        .billingAmount(new BigDecimal("95000"))
                        .unpaidAmount(new BigDecimal("5000"))
                        .build()
        );
    }
    
    public static List<InvoiceMaster> createMockInvoices(String accountNumber, LocalDate billingDate) {
        return List.of(
                InvoiceMaster.builder()
                        .accountNumber(accountNumber)
                        .billingDate(billingDate)
                        .serviceManagementNumber("SVC001")
                        .totalBillingAmount(new BigDecimal("100000"))
                        .billingAmount(new BigDecimal("95000"))
                        .unpaidAmount(new BigDecimal("5000"))
                        .adjustmentAmount(new BigDecimal("0"))
                        .unpaidStatusCode(UnpaidStatusCode.OPEN)
                        .build()
        );
    }
    
    public static InvoiceMaster createMockInvoiceMasterWithDetails(String accountNumber, LocalDate billingDate, String serviceManagementNumber) {
        List<InvoiceDetail> details = new ArrayList<>();
        details.add(InvoiceDetail.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate.toString())
                .serviceManagementNumber(serviceManagementNumber)
                .revenueItemCode("REV001")
                .billingSequence(new BigDecimal("1"))
                .billingClassificationCode(BillingClassificationCode.INVOICE)
                .billingAmount(new BigDecimal("3000"))
                .unpaidAmount(new BigDecimal("2000"))
                .build());
        
        return InvoiceMaster.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .totalBillingAmount(new BigDecimal("10000"))
                .billingAmount(new BigDecimal("9500"))
                .unpaidAmount(new BigDecimal("5000"))
                .adjustmentAmount(new BigDecimal("0"))
                .unpaidStatusCode(UnpaidStatusCode.OPEN)
                .invoiceDetails(details)
                .build();
    }
    
    public static InvoiceMaster createMockInvoiceMasterWithMultipleDetails(String accountNumber, LocalDate billingDate, String serviceManagementNumber) {
        List<InvoiceDetail> details = new ArrayList<>();
        details.add(InvoiceDetail.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate.toString())
                .serviceManagementNumber(serviceManagementNumber)
                .revenueItemCode("REV001")
                .billingSequence(new BigDecimal("1"))
                .billingClassificationCode(BillingClassificationCode.INVOICE)
                .billingAmount(new BigDecimal("3000"))
                .unpaidAmount(new BigDecimal("2000"))
                .build());
        details.add(InvoiceDetail.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate.toString())
                .serviceManagementNumber(serviceManagementNumber)
                .revenueItemCode("REV002")
                .billingSequence(new BigDecimal("2"))
                .billingClassificationCode(BillingClassificationCode.INVOICE)
                .billingAmount(new BigDecimal("2000"))
                .unpaidAmount(new BigDecimal("1500"))
                .build());
        
        return InvoiceMaster.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .totalBillingAmount(new BigDecimal("10000"))
                .billingAmount(new BigDecimal("9500"))
                .unpaidAmount(new BigDecimal("3500"))
                .adjustmentAmount(new BigDecimal("0"))
                .unpaidStatusCode(UnpaidStatusCode.OPEN)
                .invoiceDetails(details)
                .build();
    }
}
