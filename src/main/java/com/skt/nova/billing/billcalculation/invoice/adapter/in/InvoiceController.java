package com.skt.nova.billing.billcalculation.invoice.adapter.in;

import com.skt.nova.billing.billcalculation.invoice.api.InvoiceQueryUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    
    private final InvoiceQueryUseCase invoiceQueryUseCase;
    
    /**
     * 계정번호로 청구 정보 요약 조회 (계정번호, 청구일자별 그룹화)
     * @param accountNumber 계정번호
     * @return 해당 계정번호에 해당하는 청구 정보 요약 (계정번호, 청구일자별 그룹화)
     */
    @GetMapping("/account/{accountNumber}/summary")
    public ResponseEntity<List<InvoiceSummaryDto>> getInvoiceSummaryByAccountNumber(@PathVariable String accountNumber) {
        List<InvoiceSummaryDto> summaries = invoiceQueryUseCase.findInvoiceSummaryByAccountNumber(accountNumber);
        return ResponseEntity.ok(summaries);
    }
    
    /**
     * 계정번호와 청구일자로 청구 정보 상세 조회
     * @param accountNumber 계정번호
     * @param billingDate 청구일자
     * @return 해당 계정번호 및 청구일자에 해당하는 청구 마스터 정보와 그 하위 상세 정보
     */
    @GetMapping("/account/{accountNumber}/billing-date/{billingDate}")
    public ResponseEntity<List<InvoiceMasterDto>> getInvoicesByAccountNumberAndBillingDate(
            @PathVariable String accountNumber,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate billingDate) {
        
        List<InvoiceMasterDto> invoices = invoiceQueryUseCase.findInvoicesByAccountNumberAndBillingDate(accountNumber, billingDate);
        return ResponseEntity.ok(invoices);
    }
} 