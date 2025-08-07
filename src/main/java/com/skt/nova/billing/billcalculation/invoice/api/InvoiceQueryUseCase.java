package com.skt.nova.billing.billcalculation.invoice.api;

import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceQueryUseCase {
    
    /**
     * 계정번호로 청구 정보 요약 조회 (계정번호, 청구일자별 그룹화)
     * @param accountNumber 계정번호
     * @return 해당 계정번호에 해당하는 청구 정보 요약 (계정번호, 청구일자별 그룹화)
     */
    List<InvoiceSummaryDto> findInvoiceSummaryByAccountNumber(String accountNumber);
    
    /**
     * 계정번호와 청구일자로 청구 정보 상세 조회
     * @param accountNumber 계정번호
     * @param billingDate 청구일자
     * @return 해당 계정번호 및 청구일자에 해당하는 청구 마스터 정보와 그 하위 상세 정보
     */
    List<InvoiceMasterDto> findInvoicesByAccountNumberAndBillingDate(String accountNumber, LocalDate billingDate);

} 