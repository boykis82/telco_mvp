package com.skt.nova.billing.billcalculation.adjustment.api;

import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentResponse;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentApprovalRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentRejectionRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentCancelRequest;

/**
 * 요금 조정 관리 Command Port 인터페이스
 * 요금 조정 생성, 수정, 삭제 등의 명령 작업을 정의합니다.
 */
public interface AdjustmentCommandUseCase {
    // 요금 조정 관련 명령 작업 메서드들이 정의될 예정
    AfterAdjustmentResponse requestAfterAdjustment(AfterAdjustmentRequest afterAdjustmentRequest);
    
    /**
     * 승인 요청 상태인 조정 내역을 승인
     */
    void approveAdjustment(AdjustmentApprovalRequest request);
    
    /**
     * 승인 요청 상태인 조정 내역을 반려
     */
    void rejectAdjustment(AdjustmentRejectionRequest request);
    
    /**
     * 승인 상태인 조정 내역을 취소
     */
    void cancelAdjustment(AdjustmentCancelRequest request);
} 