package com.skt.nova.billing.billcalculation.adjustmentauthorization.api;

public interface AdjustmentAuthorizationPort {
    AuthorizationResult checkAdjustmentAuthorization(String adjustmentRequestUserId, long adjustmentRequestAmount);
} 