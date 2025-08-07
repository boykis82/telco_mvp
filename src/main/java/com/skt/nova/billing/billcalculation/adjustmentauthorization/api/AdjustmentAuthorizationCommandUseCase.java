package com.skt.nova.billing.billcalculation.adjustmentauthorization.api;

public interface AdjustmentAuthorizationCommandUseCase {
    AuthorizationResult checkAdjustmentAuthorization(String adjustmentRequestUserId, long adjustmentRequestAmount);
} 