package com.skt.nova.billing.billcalculation.adjustment.port.out;

public interface VocSystemPort {
    void updateVocStatus(VocStatusUpdateRequest request);
}
