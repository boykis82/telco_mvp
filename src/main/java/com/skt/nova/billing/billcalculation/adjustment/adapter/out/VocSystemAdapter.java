package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.port.out.VocStatusUpdateRequest;
import com.skt.nova.billing.billcalculation.adjustment.port.out.VocSystemPort;

@Component
public class VocSystemAdapter implements VocSystemPort {
    @Override
    public void updateVocStatus(VocStatusUpdateRequest request) {
        // TODO Auto-generated method stub
    }
}
