package com.skt.nova.billing.billcalculation.payment.adapter.out;

import java.math.BigDecimal;
import java.util.List;

import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyPaymentDto;
import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.invoice.application.ApplyPaymentResultDto;
import com.skt.nova.billing.billcalculation.payment.port.out.InvoiceClientPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceClientAdapter implements InvoiceClientPort {

    private final InvoiceCommandUseCase invoiceCommandUseCase;

    @Override
    public List<ApplyPaymentResultDto> applyPayment(String accountNumber, BigDecimal paymentAmount) {
        ApplyPaymentDto applyPaymentDto = new ApplyPaymentDto(accountNumber, paymentAmount);
        return invoiceCommandUseCase.applyPayment(applyPaymentDto);
    }

    @Override
    public void applyRefund(ApplyPaymentDto applyRefundRequestDto) {

    }
}
