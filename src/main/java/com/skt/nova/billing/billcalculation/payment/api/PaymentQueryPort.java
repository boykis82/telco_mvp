package com.skt.nova.billing.billcalculation.payment.api;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;

import java.util.List;

/**
 * 수납 관리 Query Port 인터페이스
 * 수납 조회 등의 쿼리 작업을 정의합니다.
 */
public interface PaymentQueryPort {
    
    /**
     * 계정번호로 수납 내역 조회
     * @param accountNumber 계정번호
     * @return 해당 계정번호의 수납 내역 요약 (수납일시, 수납구분별 그룹화)
     */
    List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber);
} 