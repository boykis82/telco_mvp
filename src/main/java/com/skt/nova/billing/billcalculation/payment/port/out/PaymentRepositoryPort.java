package com.skt.nova.billing.billcalculation.payment.port.out;

import java.util.List;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;

public interface PaymentRepositoryPort {

    public void save(PaymentMaster paymentMaster);
    
    public void saveAll(List<PaymentMaster> paymentMasters);

    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber);
}
