package com.skt.nova.billing.billcalculation.adjustment.application;

import com.skt.nova.billing.billcalculation.adjustment.api.AdjustmentQueryUseCase;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.*;
import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdjustmentQueryUseCaseImpl implements AdjustmentQueryUseCase {
    private final AdjustmentQueryRepositoryPort adjustmentQueryRepositoryPort;
    private final AdjustmentDomainDtoMapper adjustmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdjustmentDto> findByServiceManagementNumber(String serviceManagementNumber) {
        List<Adjustment> domains = adjustmentQueryRepositoryPort
                .findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber);
        
        return domains.stream()
                .map(adjustmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

} 