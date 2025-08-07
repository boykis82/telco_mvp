package com.skt.nova.billing.billcalculation.approvalrequest.api;

public interface ApprovalRequestCommandPort {
    String requestApproval(String adjustmentRequestUserId, long adjustmentRequestAmount);
} 