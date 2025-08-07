package com.skt.nova.billing.billcalculation.invoice.application;

import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceQueryUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyPaymentDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.domain.AdjustmentItem;
import com.skt.nova.billing.billcalculation.invoice.domain.ApplyPaymentResult;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterId;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceQueryRepositoryPort;
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
public class InvoiceQueryUseCaseImpl implements InvoiceQueryUseCase {

    private final InvoiceQueryRepositoryPort invoiceQueryRepositoryPort;
    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceSummaryDto> findInvoiceSummaryByAccountNumber(String accountNumber) {
        return invoiceQueryRepositoryPort.findInvoiceSummaryByAccountNumber(accountNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMasterDto> findInvoicesByAccountNumberAndBillingDate(String accountNumber, LocalDate billingDate) {
        List<InvoiceMaster> invoices = invoiceQueryRepositoryPort.findInvoicesByAccountNumberAndBillingDate(accountNumber, billingDate);
        return invoices.stream()
                .map(invoiceMapper::mapToDto)
                .collect(Collectors.toList());
    }
} 