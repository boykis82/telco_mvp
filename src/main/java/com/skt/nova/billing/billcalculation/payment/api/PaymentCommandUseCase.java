package com.skt.nova.billing.billcalculation.payment.api;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentRequestDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentResponseDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.RefundRequestDto;

import java.util.List;

/**
 * 수납 관리 Command Port 인터페이스
 * 수납 생성, 수정, 삭제 등의 명령 작업을 정의합니다.
 */
public interface PaymentCommandUseCase {
    
    /**
     * 수납 처리
     * @param paymentRequestDto 수납 요청 정보
     * @return 수납 처리 결과 목록
     */
    List<PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto);   
    
    /**
     * 환불 처리
     * @param RefundRequestDto 환불 요청 정보
     * @return 환불된 금액
     */    
    Long processRefund(RefundRequestDto refundRequestDto);
} 