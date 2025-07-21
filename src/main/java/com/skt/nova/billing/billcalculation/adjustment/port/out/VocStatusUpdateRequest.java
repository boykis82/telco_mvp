package com.skt.nova.billing.billcalculation.adjustment.port.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocStatusUpdateRequest {
    String vocId;
    String statusCode;
}
