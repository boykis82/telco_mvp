package com.skt.nova.billing.billcalculation.payment.adapter.in;

import com.skt.nova.billing.billcalculation.payment.api.PaymentCommandPort;
import com.skt.nova.billing.billcalculation.payment.api.PaymentQueryPort;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentRequestDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentResponseDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentCommandPort paymentCommandPort;
    private final PaymentQueryPort paymentQueryPort;
    
    /**
     * 수납 처리
     * @param paymentRequestDto 수납 요청 정보
     * @return 수납 처리 결과
     */
    @PostMapping("/")
    public ResponseEntity<List<PaymentResponseDto>> processPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        List<PaymentResponseDto> result = paymentCommandPort.processPayment(paymentRequestDto);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 계정번호로 수납 내역 조회
     * @param accountNumber 계정번호
     * @return 수납 내역 요약 목록
     */
    @GetMapping("/summary/{accountNumber}")
    public ResponseEntity<List<PaymentSummaryDto>> getPaymentSummary(@PathVariable String accountNumber) {
        List<PaymentSummaryDto> result = paymentQueryPort.findPaymentSummaryByAccountNumber(accountNumber);
        return ResponseEntity.ok(result);
    }
} 