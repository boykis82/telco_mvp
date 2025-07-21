package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.port.out.ApprovalClientPort;
import com.skt.nova.billing.billcalculation.approvalrequest.api.ApprovalRequestPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApprovalClientAdapter implements ApprovalClientPort {

    private final ApprovalRequestPort approvalRequestPort;

    @Override
    public String requestApproval(String adjustmentRequestUserId, long adjustmentRequestAmount) {
        return approvalRequestPort.requestApproval(adjustmentRequestUserId, adjustmentRequestAmount);
    }
}
