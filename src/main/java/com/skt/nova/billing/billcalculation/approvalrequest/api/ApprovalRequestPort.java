package com.skt.nova.billing.billcalculation.approvalrequest.api;

public interface ApprovalRequestPort {
    String requestApproval(String adjustmentRequestUserId, long adjustmentRequestAmount);
} 