package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.port.out.ApprovalClientPort;
import com.skt.nova.billing.billcalculation.approvalrequest.api.ApprovalRequestCommandPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApprovalClientAdapter implements ApprovalClientPort {

    private final ApprovalRequestCommandPort approvalRequestCommandPort;

    @Override
    public String requestApproval(String adjustmentRequestUserId, long adjustmentRequestAmount) {
        return approvalRequestCommandPort.requestApproval(adjustmentRequestUserId, adjustmentRequestAmount);
    }
}
