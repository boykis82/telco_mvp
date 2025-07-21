package com.skt.nova.billing.billcalculation.adjustment.port.out;

import java.time.LocalDateTime;
import java.util.List;

import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;

public interface AdjustmentRepositoryPort {
    public void saveAll(List<Adjustment> adjustments);
    public List<Adjustment> findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(String serviceManagementNumber);
    
    /**
     * adjustmentRequestId로 조정 내역 조회
     */
    List<Adjustment> findByAdjustmentRequestId(String adjustmentRequestId);
    
    /**
     * 승인자 ID로 조정 내역 조회
     */
    List<Adjustment> findByAdjustmentApproveUserId(String adjustmentApproveUserId);
    
    /**
     * 서비스관리번호, 계좌번호, 요청일시로 조정 내역 조회
     */
    List<Adjustment> findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
            String serviceManagementNumber, 
            String accountNumber, 
            LocalDateTime adjustmentRequestDateTime);
}
