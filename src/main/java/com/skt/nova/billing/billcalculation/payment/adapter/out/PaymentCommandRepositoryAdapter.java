package com.skt.nova.billing.billcalculation.payment.adapter.out;

import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMapper;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterJpaEntity;
import com.skt.nova.billing.billcalculation.payment.infrastructure.PaymentMasterJpaRepository;
import com.skt.nova.billing.billcalculation.payment.port.out.PaymentCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentCommandRepositoryAdapter implements PaymentCommandRepositoryPort {
        
    private final PaymentMasterJpaRepository paymentMasterJpaRepository;
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
}