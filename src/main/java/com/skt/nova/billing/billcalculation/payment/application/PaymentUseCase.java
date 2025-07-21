package com.skt.nova.billing.billcalculation.payment.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skt.nova.billing.billcalculation.invoice.application.ApplyPaymentResultDto;
import com.skt.nova.billing.billcalculation.payment.api.PaymentCommandPort;
import com.skt.nova.billing.billcalculation.payment.api.PaymentQueryPort;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentRequestDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentResponseDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.PaymentSummaryDto;
import com.skt.nova.billing.billcalculation.payment.api.dto.RefundRequestDto;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentClassificationCode;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentDetail;
import com.skt.nova.billing.billcalculation.payment.domain.PaymentMaster;
import com.skt.nova.billing.billcalculation.payment.port.out.InvoiceClientPort;
import com.skt.nova.billing.billcalculation.payment.port.out.PaymentRepositoryPort;

import lombok.RequiredArgsConstructor;

/**
 * 수납 관리 Application Service
 * 수납 관련 비즈니스 로직을 처리하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class PaymentUseCase implements PaymentCommandPort, PaymentQueryPort {

    private final InvoiceClientPort invoiceClientPort;
    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentSummaryDto> findPaymentSummaryByAccountNumber(String accountNumber) {
        return paymentRepositoryPort.findPaymentSummaryByAccountNumber(accountNumber);
    }

    @Override
    @Transactional
    public List<PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto) {
        // invoice domain에 수납 내역 반영 후 결과 반환
        List<ApplyPaymentResultDto> applyPaymentResultDtos = 
            invoiceClientPort.applyPayment(paymentRequestDto.getAccountNumber(), paymentRequestDto.getPaymentAmount());

        // payment domain에 수납 내역 반영
        for (ApplyPaymentResultDto applyPaymentResultDto : applyPaymentResultDtos) {
            PaymentMaster paymentMaster = createPaymentMaster(paymentRequestDto, applyPaymentResultDto);
            // 한방 수납 아니면 수납상세 생성
            if (!applyPaymentResultDto.isFullyPaidAtOnce()) {
                List<PaymentDetail> paymentDetails = createPaymentDetails(paymentRequestDto, applyPaymentResultDto);
                paymentMaster.addPaymentDetails(paymentDetails);
            }
            paymentRepositoryPort.save(paymentMaster);
        }
        return toPaymentResponseDtos(applyPaymentResultDtos);
    }

    private PaymentMaster createPaymentMaster(PaymentRequestDto paymentRequestDto, ApplyPaymentResultDto applyPaymentResultDto) {
        return PaymentMaster.builder()
            .accountNumber(applyPaymentResultDto.getAccountNumber())
            .serviceManagementNumber(applyPaymentResultDto.getServiceManagementNumber())
            .paymentDateTime(paymentRequestDto.getPaymentDateTime())                
            .paymentClassificationCode(PaymentClassificationCode.PAYMENT)
            .paymentAmount(applyPaymentResultDto.getPaymentAmount())                
            .billingDate(applyPaymentResultDto.getBillingDate())
            .build();
    }

    private List<PaymentDetail> createPaymentDetails(PaymentRequestDto paymentRequestDto, ApplyPaymentResultDto applyPaymentResultDto) {
        return applyPaymentResultDto.getApplyPaymentDetailResults().stream()
            .map(applyPaymentDetailResultDto -> PaymentDetail.builder()
                .accountNumber(applyPaymentResultDto.getAccountNumber())
                .serviceManagementNumber(applyPaymentResultDto.getServiceManagementNumber())
                .paymentDateTime(paymentRequestDto.getPaymentDateTime())
                .billingDate(applyPaymentResultDto.getBillingDate())
                .revenueItemCode(applyPaymentDetailResultDto.getRevenueItemCode())
                .billingSequence(applyPaymentDetailResultDto.getBillingSequence())
                .paymentAmount(applyPaymentDetailResultDto.getPaymentAmount())
                .build())
            .collect(Collectors.toList());
    }
        
    private List<PaymentResponseDto> toPaymentResponseDtos(List<ApplyPaymentResultDto> applyPaymentResultDtos) {
        return applyPaymentResultDtos.stream()
            .map(applyPaymentResultDto -> PaymentResponseDto.builder()
                .accountNumber(applyPaymentResultDto.getAccountNumber())
                .serviceManagementNumber(applyPaymentResultDto.getServiceManagementNumber())
                .billingDate(applyPaymentResultDto.getBillingDate())
                .paymentAmount(applyPaymentResultDto.getPaymentAmount())
                .build())
            .collect(Collectors.toList());        
    }

    /**
     * 환불 처리
     * @param RefundRequestDto 환불 요청 정보
     * @return 환불된 금액
     */    
    @Override
    @Transactional
    public Long processRefund(RefundRequestDto refundRequestDto) {
        // 1. 입력으로 들어온 계정번호와 수납일시에 해당되는 수납master/detail을 조회한다.
        // 2. 조회된 데이터를 copy하여 환불이력을 쌓는다. 환불이력은 수납이력과 동일한 형태로 쌓는다. 수납detail이 있었으면 환불detail도 쌓는다.
        // 3. invoiceClientPort의 processRefund를 호출한다. 입력은 계정번호,서비스관리번호,청구일자,list of [매출항목코드,수납금액]이다.
        throw new UnsupportedOperationException("Not implemented");
    }
} 