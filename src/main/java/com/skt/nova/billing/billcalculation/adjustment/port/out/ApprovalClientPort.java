package com.skt.nova.billing.billcalculation.adjustment.port.out;

public interface ApprovalClientPort {
    String requestApproval(String adjustmentRequestUserId, long adjustmentRequestAmount);
}
