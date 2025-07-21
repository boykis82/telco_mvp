package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import com.skt.nova.billing.billcalculation.invoice.domain.BillingClassificationCode;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceDetail;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {
    
    public InvoiceMaster mapToDomain(InvoiceMasterJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return InvoiceMaster.builder()
                .accountNumber(entity.getAccountNumber())
                .billingDate(entity.getBillingDate())
                .serviceManagementNumber(entity.getServiceManagementNumber())
                .totalBillingAmount(entity.getTotalBillingAmount())
                .billingAmount(entity.getBillingAmount())
                .unpaidAmount(entity.getUnpaidAmount())
                .adjustmentAmount(entity.getAdjustmentAmount())
                .unpaidStatusCode(entity.getUnpaidStatusCode())
                .invoiceDetails(mapDetailsToDomain(entity.getInvoiceDetails()))
                .build();
    }
    
    public List<InvoiceDetail> mapDetailsToDomain(List<InvoiceDetailJpaEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        
        return entities.stream()
                .map(this::mapDetailToDomain)
                .collect(Collectors.toList());
    }
    
    public InvoiceDetail mapDetailToDomain(InvoiceDetailJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return InvoiceDetail.builder()
                .accountNumber(entity.getAccountNumber())
                .billingDate(entity.getBillingDate())
                .serviceManagementNumber(entity.getServiceManagementNumber())
                .revenueItemCode(entity.getRevenueItemCode())
                .billingSequence(entity.getBillingSequence())
                .billingClassificationCode(BillingClassificationCode.valueOf(entity.getBillingClassificationCode()))
                .billingAmount(entity.getBillingAmount())
                .unpaidAmount(entity.getUnpaidAmount())
                .build();
    }
    
    public InvoiceMasterJpaEntity mapToEntity(InvoiceMaster domain) {
        if (domain == null) {
            return null;
        }
        
        return InvoiceMasterJpaEntity.builder()
                .accountNumber(domain.getAccountNumber())
                .billingDate(domain.getBillingDate())
                .serviceManagementNumber(domain.getServiceManagementNumber())
                .totalBillingAmount(domain.getTotalBillingAmount())
                .billingAmount(domain.getBillingAmount())
                .unpaidAmount(domain.getUnpaidAmount())
                .adjustmentAmount(domain.getAdjustmentAmount())
                .unpaidStatusCode(domain.getUnpaidStatusCode())
                .build();
    }
    
    public InvoiceDetailJpaEntity mapDetailToEntity(InvoiceDetail domain) {
        if (domain == null) {
            return null;
        }
        
        return InvoiceDetailJpaEntity.builder()
                .accountNumber(domain.getAccountNumber())
                .billingDate(domain.getBillingDate())
                .serviceManagementNumber(domain.getServiceManagementNumber())
                .revenueItemCode(domain.getRevenueItemCode())
                .billingSequence(domain.getBillingSequence())
                .billingClassificationCode(domain.getBillingClassificationCode().getCode())
                .billingAmount(domain.getBillingAmount())
                .unpaidAmount(domain.getUnpaidAmount())
                .build();
    }
} 