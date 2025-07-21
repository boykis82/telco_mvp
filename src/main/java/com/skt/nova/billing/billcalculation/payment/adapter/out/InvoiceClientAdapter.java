package com.skt.nova.billing.billcalculation.payment.adapter.out;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.invoice.application.ApplyPaymentResultDto;
import com.skt.nova.billing.billcalculation.payment.port.out.InvoiceClientPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceClientAdapter implements InvoiceClientPort {

    private final InvoiceClientPort invoiceClientPort;

    @Override
    public List<ApplyPaymentResultDto> applyPayment(String accountNumber, BigDecimal paymentAmount) {
        return invoiceClientPort.applyPayment(accountNumber, paymentAmount);
    }
}
