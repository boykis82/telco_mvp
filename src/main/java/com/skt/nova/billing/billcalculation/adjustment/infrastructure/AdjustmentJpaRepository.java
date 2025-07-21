package com.skt.nova.billing.billcalculation.adjustment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdjustmentJpaRepository extends JpaRepository<AdjustmentJpaEntity, AdjustmentId> {
    
    List<AdjustmentJpaEntity> findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(String serviceManagementNumber);
    
    List<AdjustmentJpaEntity> findByAdjustmentRequestId(String adjustmentRequestId);
    
    List<AdjustmentJpaEntity> findByAdjustmentApproveUserId(String adjustmentApproveUserId);
    
    List<AdjustmentJpaEntity> findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
            String serviceManagementNumber, 
            String accountNumber, 
            LocalDateTime adjustmentRequestDateTime);
} 