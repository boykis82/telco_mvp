package com.skt.nova.billing.billcalculation.invoice.adapter.out;

import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceDetailJpaEntity;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMapper;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterJpaEntity;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterJpaRepository;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceCommandRepositoryAdapter implements InvoiceCommandRepositoryPort {
    
    private final InvoiceMasterJpaRepository invoiceMasterJpaRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public void save(InvoiceMaster domain) {
        InvoiceMasterJpaEntity updatedEntity = invoiceMapper.mapToEntity(domain);
        List<InvoiceDetailJpaEntity> detailEntities = domain.getInvoiceDetails().stream()
                .map(invoiceMapper::mapDetailToEntity)
                .toList();
        updatedEntity.setInvoiceDetails(detailEntities);
        invoiceMasterJpaRepository.save(updatedEntity);        
    }
}