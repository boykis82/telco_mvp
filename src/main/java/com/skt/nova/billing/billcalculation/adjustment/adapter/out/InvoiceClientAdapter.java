package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandPort;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.adjustment.port.out.InvoiceClientPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InvoiceClientAdapter implements InvoiceClientPort {

    private final InvoiceCommandPort invoiceCommandPort;

    @Override
    public void applyAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto) {
        invoiceCommandPort.applyAdjustment(applyAdjustmentRequestDto);
    }

    @Override
    public void cancelAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto) {
        invoiceCommandPort.cancelAdjustment(applyAdjustmentRequestDto);
    }

}
