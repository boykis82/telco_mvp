package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import java.util.List;
import java.util.stream.Collectors;

import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentCommandRepositoryPort;
import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentJpaEntity;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentJpaRepository;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentDomainEntityMapper;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentQueryRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdjustmentCommandRepositoryAdapter implements AdjustmentCommandRepositoryPort {
    private final AdjustmentJpaRepository adjustmentJpaRepository;
    private final AdjustmentDomainEntityMapper adjustmentMapper;

    @Override
    public void saveAll(List<Adjustment> adjustments) {
        List<AdjustmentJpaEntity> entities = adjustments.stream().map(adjustmentMapper::mapToJpaEntity).collect(Collectors.toList());
        adjustmentJpaRepository.saveAll(entities);
    }
}
