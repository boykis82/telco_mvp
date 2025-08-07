package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.adjustment.port.out.InvoiceClientPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InvoiceClientAdapter implements InvoiceClientPort {

    private final InvoiceCommandUseCase invoiceCommandUseCase;

    @Override
    public void applyAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto) {
        invoiceCommandUseCase.applyAdjustment(applyAdjustmentRequestDto);
    }

    @Override
    public void cancelAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto) {
        invoiceCommandUseCase.cancelAdjustment(applyAdjustmentRequestDto);
    }

}
