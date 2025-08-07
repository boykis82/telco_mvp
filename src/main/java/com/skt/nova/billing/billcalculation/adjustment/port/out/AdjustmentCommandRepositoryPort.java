package com.skt.nova.billing.billcalculation.adjustment.port.out;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;

import java.time.LocalDateTime;
import java.util.List;

public interface AdjustmentCommandRepositoryPort {
    void saveAll(List<Adjustment> adjustments);
}
