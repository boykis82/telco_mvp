package com.skt.nova.billing.billcalculation.adjustment.port.out;

import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;

public interface InvoiceClientPort {
    void applyAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto);
    
    /**
     * 조정 취소 반영
     */
    void cancelAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto);
    
}
