package com.skt.nova.billing.billcalculation.invoice.api;

import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyPaymentDto;
import com.skt.nova.billing.billcalculation.invoice.application.ApplyPaymentResultDto;

import java.util.List;

/**
 * 요금 정보 관리 Command Port 인터페이스
 * 요금 정보 생성, 수정, 삭제 등의 명령 작업을 정의합니다.
 */
public interface InvoiceCommandUseCase {
    // 요금 정보 관련 명령 작업 메서드들이 정의될 예정

    void applyAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto);
    
    /**
     * 조정 취소 반영
     */
    void cancelAdjustment(ApplyAdjustmentRequestDto applyAdjustmentRequestDto);
    
    /**
     * 수납 적용
     * @param accountNumber 계정번호
     * @param paymentAmount 수납금액
     * @return 수납 적용 결과 목록
     */
    List<ApplyPaymentResultDto> applyPayment(ApplyPaymentDto applyPaymentDto);
} 