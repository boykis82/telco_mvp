package com.skt.nova.billing.billcalculation.payment.infrastructure;

import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentMasterQueryRepository {
    
    /**
     * 계정번호로 수납 내역 조회 (수납일시, 수납구분별 그룹화)
     * @param accountNumber 계정번호
     * @return 수납 내역 요약 목록
     */
    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber) {
        // TODO: QueryDSL Q클래스 생성 후 실제 구현
        // 현재는 기본 구현으로 빈 리스트 반환
        
        return List.of();
    }
} 