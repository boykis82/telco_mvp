package com.skt.nova.billing.billcalculation.payment.adapter.out;

import java.util.List;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMapper;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterJpaEntity;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterJpaRepository;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterQueryRepository;
import com.skt.nova.billing.billcalculation.payment.port.out.PaymentRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {
        
    private final PaymentMasterJpaRepository paymentMasterJpaRepository;
    private final PaymentMasterQueryRepository paymentMasterQueryRepository;
    private final PaymentMapper paymentMapper;
    
    @Override
    public void save(PaymentMaster paymentMaster) {
        PaymentMasterJpaEntity entity = paymentMapper.mapToJpaEntity(paymentMaster);
        paymentMasterJpaRepository.save(entity);
    }
    
    @Override
    public void saveAll(List<PaymentMaster> paymentMasters) {
        List<PaymentMasterJpaEntity> entities = paymentMapper.mapToPaymentMasterJpaEntityList(paymentMasters);
        paymentMasterJpaRepository.saveAll(entities);
    }

    @Override
    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber) {
        return paymentMasterQueryRepository.findPaymentSummaryByAccountNumber(accountNumber);
    }
}
