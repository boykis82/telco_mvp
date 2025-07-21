package com.skt.nova.billing.billcalculation.invoice.application;

import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandPort;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceQueryPort;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyPaymentDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.domain.AdjustmentItem;
import com.skt.nova.billing.billcalculation.invoice.domain.ApplyPaymentResult;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceRepositoryPort;
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
public class InvoiceUseCase implements InvoiceQueryPort, InvoiceCommandPort {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    @Override
    public void applyAdjustment(ApplyAdjustmentRequestDto dto) {
        InvoiceMasterId id = new InvoiceMasterId(
                dto.getAccountNumber(),
                LocalDate.parse(dto.getBillingDate()),
                dto.getServiceManagementNumber()
        );
        InvoiceMaster domain = invoiceRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException("청구 정보를 찾을 수 없습니다."));
        
        // DTO를 도메인 값 객체로 변환
        List<AdjustmentItem> adjustmentItems = dto.getItems().stream()
                .map(itemDto -> AdjustmentItem.builder()
                        .revenueItemCode(itemDto.getRevenueItemCode())
                        .adjustmentRequestAmount(itemDto.getAdjustmentRequestAmount())
                        .build())
                .collect(Collectors.toList());
        
        domain.applyAdjustment(adjustmentItems);
        invoiceRepositoryPort.save(domain);
    }
    
    @Transactional
    @Override
    public void cancelAdjustment(ApplyAdjustmentRequestDto dto) {
        InvoiceMasterId id = new InvoiceMasterId(
                dto.getAccountNumber(),
                LocalDate.parse(dto.getBillingDate()),
                dto.getServiceManagementNumber()
        );
        InvoiceMaster domain = invoiceRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException("청구 정보를 찾을 수 없습니다."));
        
        // DTO를 도메인 값 객체로 변환
        List<AdjustmentItem> adjustmentItems = dto.getItems().stream()
                .map(itemDto -> AdjustmentItem.builder()
                        .revenueItemCode(itemDto.getRevenueItemCode())
                        .adjustmentRequestAmount(itemDto.getAdjustmentRequestAmount())
                        .build())
                .collect(Collectors.toList());
        
        domain.cancelAdjustment(adjustmentItems);
        invoiceRepositoryPort.save(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceSummaryDto> findInvoiceSummaryByAccountNumber(String accountNumber) {
        return invoiceRepositoryPort.findInvoiceSummaryByAccountNumber(accountNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMasterDto> findInvoicesByAccountNumberAndBillingDate(String accountNumber, LocalDate billingDate) {
        List<InvoiceMaster> invoices = invoiceRepositoryPort.findInvoicesByAccountNumberAndBillingDate(accountNumber, billingDate);
        return invoices.stream()
                .map(invoiceMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ApplyPaymentResultDto> applyPayment(ApplyPaymentDto applyPaymentDto) {
        List<ApplyPaymentResultDto> results = new ArrayList<>();
        List<InvoiceMaster> invoices = invoiceRepositoryPort.findUnpaidInvoiceMastersByAccountNumber(applyPaymentDto.accountNumber());

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