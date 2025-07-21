package com.skt.nova.billing.billcalculation.payment.port.out;

import java.math.BigDecimal;
import java.util.List;

import com.skt.nova.billing.billcalculation.invoice.application.ApplyPaymentResultDto;

public interface InvoiceClientPort {
    List<ApplyPaymentResultDto> applyPayment(String accountNumber, BigDecimal paymentAmount);
    void applyRefund(ApplyRefundRequestDto applyRefundRequestDto);
}
