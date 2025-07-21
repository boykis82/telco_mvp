package com.skt.nova.billing.billcalculation.adjustment.port.out;

import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AuthorizationResult;

public interface AdjustmentAuthorizationClientPort {
    AuthorizationResult checkAdjustmentAuthorization(String adjustmentRequestUserId, long adjustmentRequestAmount);
}
