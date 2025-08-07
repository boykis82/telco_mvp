package com.skt.nova.billing.billcalculation.payment.port.out;

import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;

import java.util.List;

public interface PaymentCommandRepositoryPort {

    void save(PaymentMaster paymentMaster);
    
    void saveAll(List<PaymentMaster> paymentMasters);
}