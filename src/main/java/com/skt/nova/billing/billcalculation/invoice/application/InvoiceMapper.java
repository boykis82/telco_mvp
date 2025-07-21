package com.skt.nova.billing.billcalculation.invoice.application;

import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceDetailDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceDetail;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {
    
    public InvoiceMasterDto mapToDto(InvoiceMaster domain) {
        if (domain == null) {
            return null;
        }
        
        return InvoiceMasterDto.builder()
                .accountNumber(domain.getAccountNumber())
                .billingDate(domain.getBillingDate())
                .serviceManagementNumber(domain.getServiceManagementNumber())
                .totalBillingAmount(domain.getTotalBillingAmount())
                .billingAmount(domain.getBillingAmount())
                .unpaidAmount(domain.getUnpaidAmount())
                .adjustmentAmount(domain.getAdjustmentAmount())
                .unpaidStatusCode(domain.getUnpaidStatusCode().getCode())
                .invoiceDetails(mapDetailsToDto(domain.getInvoiceDetails()))
                .build();
    }
    
    public List<InvoiceDetailDto> mapDetailsToDto(List<InvoiceDetail> details) {
        if (details == null) {
            return List.of();
        }
        
        return details.stream()
                .map(this::mapDetailToDto)
                .collect(Collectors.toList());
    }
    
    public InvoiceDetailDto mapDetailToDto(InvoiceDetail detail) {
        if (detail == null) {
            return null;
        }
        
        return InvoiceDetailDto.builder()
                .accountNumber(detail.getAccountNumber())
                .billingDate(detail.getBillingDate())
                .serviceManagementNumber(detail.getServiceManagementNumber())
                .revenueItemCode(detail.getRevenueItemCode())
                .billingSequence(detail.getBillingSequence())
                .billingClassificationCode(detail.getBillingClassificationCode().getCode())
                .billingAmount(detail.getBillingAmount())
                .unpaidAmount(detail.getUnpaidAmount())
                .build();
    }
}
