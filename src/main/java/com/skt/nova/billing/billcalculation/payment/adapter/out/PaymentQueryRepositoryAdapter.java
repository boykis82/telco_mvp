package com.skt.nova.billing.billcalculation.payment.adapter.out;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterQueryRepository;
import com.skt.nova.billing.billcalculation.payment.port.out.PaymentQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentQueryRepositoryAdapter implements PaymentQueryRepositoryPort {
        
    private final PaymentMasterQueryRepository paymentMasterQueryRepository;

    @Override
    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber) {
        return paymentMasterQueryRepository.findPaymentSummaryByAccountNumber(accountNumber);
    }
}