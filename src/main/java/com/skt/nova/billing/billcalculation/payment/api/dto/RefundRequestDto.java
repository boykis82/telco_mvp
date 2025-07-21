package com.skt.nova.billing.billcalculation.payment.api.dto;

import java.time.LocalDateTime;

public record RefundRequestDto(
    String accountNumber,
    LocalDateTime paymentDateTime
) {

}
