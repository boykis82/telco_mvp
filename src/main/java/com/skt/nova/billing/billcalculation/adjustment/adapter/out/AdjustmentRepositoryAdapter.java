package com.skt.nova.billing.billcalculation.adjustment.adapter.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentJpaEntity;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentJpaRepository;
import com.skt.nova.billing.billcalculation.adjustment.infrastructure.AdjustmentDomainEntityMapper;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdjustmentRepositoryAdapter implements AdjustmentRepositoryPort {
    private final AdjustmentJpaRepository adjustmentJpaRepository;
    private final AdjustmentDomainEntityMapper adjustmentMapper;

    @Override
    public void saveAll(List<Adjustment> adjustments) {
        List<AdjustmentJpaEntity> entities = adjustments.stream().map(adjustmentMapper::mapToJpaEntity).collect(Collectors.toList());
        adjustmentJpaRepository.saveAll(entities);
    }

    @Override
    public List<Adjustment> findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(String serviceManagementNumber) {
        List<AdjustmentJpaEntity> entities = adjustmentJpaRepository
                .findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber);
        
        return entities.stream()
                .map(adjustmentMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Adjustment> findByAdjustmentRequestId(String adjustmentRequestId) {
        List<AdjustmentJpaEntity> entities = adjustmentJpaRepository.findByAdjustmentRequestId(adjustmentRequestId);
        
        return entities.stream()
                .map(adjustmentMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Adjustment> findByAdjustmentApproveUserId(String adjustmentApproveUserId) {
        List<AdjustmentJpaEntity> entities = adjustmentJpaRepository.findByAdjustmentApproveUserId(adjustmentApproveUserId);
        
        return entities.stream()
                .map(adjustmentMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Adjustment> findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
            String serviceManagementNumber, 
            String accountNumber, 
            LocalDateTime adjustmentRequestDateTime) {
        List<AdjustmentJpaEntity> entities = adjustmentJpaRepository
                .findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                        serviceManagementNumber, accountNumber, adjustmentRequestDateTime);
        
        return entities.stream()
                .map(adjustmentMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}
