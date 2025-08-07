package com.skt.nova.billing.billcalculation.invoice.api.dto;

import java.math.BigDecimal;

public record ApplyPaymentDto(String accountNumber, BigDecimal paymentAmount) {

}
