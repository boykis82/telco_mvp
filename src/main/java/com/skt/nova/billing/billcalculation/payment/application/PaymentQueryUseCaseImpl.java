package com.skt.nova.billing.billcalculation.payment.application;

import com.skt.nova.billing.billcalculation.payment.api.PaymentQueryUseCase;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import com.skt.nova.billing.billcalculation.payment.port.out.PaymentQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 수납 관리 Application Service
 * 수납 관련 비즈니스 로직을 처리하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class PaymentQueryUseCaseImpl implements PaymentQueryUseCase {

    private final PaymentQueryRepositoryPort paymentQueryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber) {
        return paymentQueryRepositoryPort.findPaymentSummaryByAccountNumber(accountNumber);
    }
} 