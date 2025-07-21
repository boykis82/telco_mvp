package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentAuthorizationClientPort;
import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AdjustmentAuthorizationPort;
import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AuthorizationResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdjustmentAuthorizationClientAdapter implements AdjustmentAuthorizationClientPort {

    private final AdjustmentAuthorizationPort adjustmentAuthorizationPort;
    @Override
    public AuthorizationResult checkAdjustmentAuthorization(String adjustmentRequestUserId, long adjustmentRequestAmount) {
        return adjustmentAuthorizationPort.checkAdjustmentAuthorization(adjustmentRequestUserId, adjustmentRequestAmount);
    }
}
