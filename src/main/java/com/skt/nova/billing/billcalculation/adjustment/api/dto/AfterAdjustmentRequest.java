package com.skt.nova.billing.billcalculation.adjustment.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AfterAdjustmentRequest {

    private String serviceManagementNumber;
    private String accountNumber;
    private String billingDate;
    private String adjustmentRequestUserId;
    private String adjustmentRequestDateTime;
    private String adjustmentReasonCode;
    private String adjustmentReasonPhrase;
    private List<AdjustmentItem> items;
    private String vocId;
} 