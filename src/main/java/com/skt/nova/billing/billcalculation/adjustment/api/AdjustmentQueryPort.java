package com.skt.nova.billing.billcalculation.adjustment.api;

import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentDto;

import java.util.List;

public interface AdjustmentQueryPort {
    List<AdjustmentDto> findByServiceManagementNumber(String serviceManagementNumber);
} 