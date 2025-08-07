package com.skt.nova.billing.billcalculation.payment.port.out;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;

import java.util.List;

public interface PaymentQueryRepositoryPort {

    List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber);
}