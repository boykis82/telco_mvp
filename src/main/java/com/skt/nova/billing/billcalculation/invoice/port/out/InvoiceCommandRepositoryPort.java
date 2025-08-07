package com.skt.nova.billing.billcalculation.invoice.port.out;

import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;

public interface InvoiceCommandRepositoryPort {
    
    /**
     * 청구 마스터 도메인 객체를 저장합니다.
     * @param domain 저장할 청구 마스터 도메인 객체
     */
    void save(InvoiceMaster domain);
}