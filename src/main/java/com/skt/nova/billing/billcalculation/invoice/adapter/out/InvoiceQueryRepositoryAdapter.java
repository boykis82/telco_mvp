package com.skt.nova.billing.billcalculation.invoice.adapter.out;

import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMapper;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterId;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterJpaEntity;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterJpaRepository;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterQueryRepository;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceQueryRepositoryAdapter implements InvoiceQueryRepositoryPort {
    
    private final InvoiceMasterJpaRepository invoiceMasterJpaRepository;
    private final InvoiceMasterQueryRepository invoiceMasterQueryRepository;
    private final InvoiceMapper invoiceMapper;
    
    @Override
    public List<InvoiceSummaryDto> findInvoiceSummaryByAccountNumber(String accountNumber) {
        return invoiceMasterQueryRepository.findInvoiceSummaryByAccountNumber(accountNumber);
    }
    
    @Override
    public List<InvoiceMaster> findInvoicesByAccountNumberAndBillingDate(String accountNumber, LocalDate billingDate) {
        List<InvoiceMasterJpaEntity> entities = invoiceMasterQueryRepository.findByAccountNumberAndBillingDateWithDetails(accountNumber, billingDate);
        return entities.stream()
                .map(invoiceMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InvoiceMaster> findById(InvoiceMasterId id) {
        Optional<InvoiceMasterJpaEntity> entity = invoiceMasterJpaRepository.findById(id);
        return entity.map(invoiceMapper::mapToDomain);
    }

    @Override
    public List<InvoiceMaster> findUnpaidInvoiceMastersByAccountNumber(String accountNumber) {
        return invoiceMasterQueryRepository.findUnpaidInvoiceMastersByAccountNumber(accountNumber).stream()
                .map(invoiceMapper::mapToDomain)
                .collect(Collectors.toList());
    }
}