package com.skt.nova.billing.billcalculation.invoice.application;

import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyPaymentDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.domain.AdjustmentItem;
import com.skt.nova.billing.billcalculation.invoice.domain.ApplyPaymentResult;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceCommandRepositoryPort;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceQueryRepositoryPort;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterId;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceCommandUseCaseImpl implements InvoiceCommandUseCase {

    private final InvoiceCommandRepositoryPort invoiceCommandRepositoryPort;
    private final InvoiceQueryRepositoryPort invoiceQueryRepositoryPort;

    @Transactional
    @Override
    public void applyAdjustment(ApplyAdjustmentRequestDto dto) {
        InvoiceMasterId id = new InvoiceMasterId(
                dto.getAccountNumber(),
                LocalDate.parse(dto.getBillingDate()),
                dto.getServiceManagementNumber()
        );
        InvoiceMaster domain = invoiceQueryRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException("청구 정보를 찾을 수 없습니다."));
        
        // DTO를 도메인 값 객체로 변환
        List<AdjustmentItem> adjustmentItems = dto.getItems().stream()
                .map(itemDto -> AdjustmentItem.builder()
                        .revenueItemCode(itemDto.getRevenueItemCode())
                        .adjustmentRequestAmount(itemDto.getAdjustmentRequestAmount())
                        .build())
                .collect(Collectors.toList());
        
        domain.applyAdjustment(adjustmentItems);
        invoiceCommandRepositoryPort.save(domain);
    }
    
    @Transactional
    @Override
    public void cancelAdjustment(ApplyAdjustmentRequestDto dto) {
        InvoiceMasterId id = new InvoiceMasterId(
                dto.getAccountNumber(),
                LocalDate.parse(dto.getBillingDate()),
                dto.getServiceManagementNumber()
        );
        InvoiceMaster domain = invoiceQueryRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException("청구 정보를 찾을 수 없습니다."));
        
        // DTO를 도메인 값 객체로 변환
        List<AdjustmentItem> adjustmentItems = dto.getItems().stream()
                .map(itemDto -> AdjustmentItem.builder()
                        .revenueItemCode(itemDto.getRevenueItemCode())
                        .adjustmentRequestAmount(itemDto.getAdjustmentRequestAmount())
                        .build())
                .collect(Collectors.toList());
        
        domain.cancelAdjustment(adjustmentItems);
        invoiceCommandRepositoryPort.save(domain);
    }

    @Override
    @Transactional
    public List<ApplyPaymentResultDto> applyPayment(ApplyPaymentDto applyPaymentDto) {
        List<ApplyPaymentResultDto> results = new ArrayList<>();
        List<InvoiceMaster> invoices = invoiceQueryRepositoryPort.findUnpaidInvoiceMastersByAccountNumber(applyPaymentDto.accountNumber());

        BigDecimal remainingAmount = applyPaymentDto.paymentAmount();

        for (InvoiceMaster invoice : invoices) {
            List<ApplyPaymentResult> paymentResults = invoice.applyPayment(applyPaymentDto.paymentAmount());
            ApplyPaymentResultDto applyPaymentResultDto = toApplyPaymentResultDto(invoice, paymentResults);

            results.add(applyPaymentResultDto);

            remainingAmount = remainingAmount.subtract(applyPaymentResultDto.getPaymentAmount());
        }

        return results;
    }

    private ApplyPaymentResultDto toApplyPaymentResultDto(InvoiceMaster invoice, List<ApplyPaymentResult> paymentResults) {
        List<ApplyPaymentDetailResultDto> applyPaymentDetailResults = paymentResults.stream()
                .map(result -> ApplyPaymentDetailResultDto.builder()
                        .revenueItemCode(result.getRevenueItemCode())
                        .paymentAmount(result.getPaymentAmount())
                        .build())
                .collect(Collectors.toList());

        return ApplyPaymentResultDto.builder()
                .accountNumber(invoice.getAccountNumber())
                .serviceManagementNumber(invoice.getServiceManagementNumber())
                .billingDate(invoice.getBillingDate())
                .fullyPaidAtOnce(invoice.isFullyPaidAtOnce())
                .paymentAmount(paymentResults.stream()
                        .map(ApplyPaymentResult::getPaymentAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .applyPaymentDetailResults(applyPaymentDetailResults)
                .build();
    }
} 