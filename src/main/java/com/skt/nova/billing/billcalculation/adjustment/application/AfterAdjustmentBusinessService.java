package com.skt.nova.billing.billcalculation.adjustment.application;

import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentResponse;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentApprovalRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentCancelRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentItem;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentRejectionRequest;
import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentReasonCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentType;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentAuthorizationClientPort;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentRepositoryPort;
import com.skt.nova.billing.billcalculation.adjustment.port.out.ApprovalClientPort;
import com.skt.nova.billing.billcalculation.adjustment.port.out.InvoiceClientPort;
import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AuthorizationResult;
import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.dto.AdjustmentItemDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AfterAdjustmentBusinessService {

    private final AdjustmentAuthorizationClientPort adjustmentAuthorizationClientPort;
    private final ApprovalClientPort approvalClientPort;
    private final InvoiceClientPort invoiceClientPort;
    private final AdjustmentRepositoryPort adjustmentRepositoryPort;

    @Transactional
    public AfterAdjustmentResponse processAfterAdjustment(AfterAdjustmentRequest request) {

        // 1. 권한 체크
        long totalAmount = request.getItems().stream()
                .mapToLong(AdjustmentItem::getAdjustmentRequestAmount)
                .sum();

        AuthorizationResult authorizationResult = adjustmentAuthorizationClientPort.checkAdjustmentAuthorization(
                request.getAdjustmentRequestUserId(), totalAmount
        );

        // 2. 승인 요청
        String approvalRequestId;
        AdjustmentStatusCode statusCode;

        if (authorizationResult == AuthorizationResult.REQUEST_APPROVAL) {
            approvalRequestId = approvalClientPort.requestApproval(request.getAdjustmentRequestUserId(), totalAmount);
            statusCode = AdjustmentStatusCode.APPROVE_REQUEST;
        } else { // APPROVE
            approvalRequestId = null;
            statusCode = AdjustmentStatusCode.APPROVE;
        }

        List<Adjustment> adjustments = request.getItems().stream().map(item ->
                Adjustment.builder()
                        .serviceManagementNumber(request.getServiceManagementNumber())
                        .adjustmentDate(LocalDateTime.parse(request.getAdjustmentRequestDateTime()).toLocalDate())
                        .revenueItemCode(item.getRevenueItemCode())
                        .adjustmentSequence(1) // todo
                        .adjustmentRequestAmount(BigDecimal.valueOf(item.getAdjustmentRequestAmount()))
                        .adjustmentType(AdjustmentType.AFTER_ADJUSTMENT)
                        .adjustmentStatusCode(statusCode)
                        .adjustmentRequestDateTime(LocalDateTime.parse(request.getAdjustmentRequestDateTime()))
                        .adjustmentReasonCode(AdjustmentReasonCode.valueOf(request.getAdjustmentReasonCode()))
                        .adjustmentReasonPhrase(request.getAdjustmentReasonPhrase())
                        .adjustmentApproveRequestUserId(request.getAdjustmentRequestUserId())
                        .accountNumber(request.getAccountNumber())
                        .billingDate(LocalDate.parse(request.getBillingDate()))
                        .adjustmentRequestId(approvalRequestId)
                        .vocId(request.getVocId())
                        .build()
        ).collect(Collectors.toList());

        // 3. 조정 내역 저장
        adjustmentRepositoryPort.saveAll(adjustments);

        // 4. 승인된 경우 과금 조정 적용
        if (authorizationResult == AuthorizationResult.APPROVE) {
            List<AdjustmentItemDto> itemDtos = adjustments.stream()
                    .map(adj -> AdjustmentItemDto.builder()
                            .revenueItemCode(adj.getRevenueItemCode())
                            .adjustmentRequestAmount(adj.getAdjustmentRequestAmount())
                            .build())
                    .collect(Collectors.toList());

            ApplyAdjustmentRequestDto applyAdjustmentRequestDto = ApplyAdjustmentRequestDto.builder()
                    .serviceManagementNumber(request.getServiceManagementNumber())
                    .accountNumber(request.getAccountNumber())
                    .billingDate(request.getBillingDate())
                    .items(itemDtos)
                    .build();

            invoiceClientPort.applyAdjustment(applyAdjustmentRequestDto);
        }

        return AfterAdjustmentResponse.builder()
                .status(statusCode.name())
                .approvalRequestId(approvalRequestId)
                .build();  
    }

    @Transactional
    public String processAdjustmentRejection(AdjustmentRejectionRequest request) {
        // 1. adjustmentRequestId로 Adjustment 조회
        List<Adjustment> adjustments = adjustmentRepositoryPort.findByAdjustmentRequestId(request.getAdjustmentRequestId());        
        if (adjustments.isEmpty()) {
            throw new BusinessException("조정 내역을 찾을 수 없습니다. adjustmentRequestId: " + request.getAdjustmentRequestId());
        }
        
        // 2. adjustmentStatusCode가 '승인요청'이 아니면 예외 던짐
        boolean hasInvalidStatus = adjustments.stream()
                .anyMatch(adjustment -> adjustment.getAdjustmentStatusCode() != AdjustmentStatusCode.APPROVE_REQUEST);        
        if (hasInvalidStatus) {
            throw new BusinessException("승인 요청 상태가 아닌 조정 내역이 포함되어 있습니다. adjustmentRequestId: " + request.getAdjustmentRequestId());
        }
        
        // 3. 조회된 Adjustment들의 adjustmentStatusCode = '반려'로 변경하고, 반려 정보 업데이트
        adjustments.forEach(adjustment -> 
                adjustment.reject(request.getAdjustmentRejectedDateTime(), request.getAdjustmentRejectUserId())
        );
        
        // 4. 변경된 Adjustment 저장
        adjustmentRepositoryPort.saveAll(adjustments);
        
        log.info("조정 내역 반려 완료. adjustmentRequestId: {}, rejectUserId: {}", 
                request.getAdjustmentRequestId(), request.getAdjustmentRejectUserId());
                
        return adjustments.get(0).getVocId();
    }

    @Transactional
    public String processAdjustmentApproval(AdjustmentApprovalRequest request) {
        // 1. adjustmentRequestId로 Adjustment 조회
        List<Adjustment> adjustments = adjustmentRepositoryPort.findByAdjustmentRequestId(request.getAdjustmentRequestId());        
        if (adjustments.isEmpty()) {
            throw new BusinessException("조정 내역을 찾을 수 없습니다. adjustmentRequestId: " + request.getAdjustmentRequestId());
        }
        
        // 2. adjustmentStatusCode가 '승인요청'이 아니면 예외 던짐
        boolean hasInvalidStatus = adjustments.stream()
                .anyMatch(adjustment -> adjustment.getAdjustmentStatusCode() != AdjustmentStatusCode.APPROVE_REQUEST);        
        if (hasInvalidStatus) {
            throw new BusinessException("승인 요청 상태가 아닌 조정 내역이 포함되어 있습니다. adjustmentRequestId: " + request.getAdjustmentRequestId());
        }
        
        // 3. 조회된 Adjustment들의 adjustmentStatusCode = '승인'으로 변경하고, 승인 정보 업데이트
        adjustments.forEach(adjustment -> 
                adjustment.approve(request.getAdjustmentApprovedDateTime(), request.getAdjustmentApproveUserId())
        );
        
        // 4. 변경된 Adjustment 저장
        adjustmentRepositoryPort.saveAll(adjustments);
        
        // 5. invoice 패키지의 invoiceUseCase의 applyAdjustment를 호출
        ApplyAdjustmentRequestDto applyRequest = buildApplyAdjustmentRequest(adjustments);
        
        invoiceClientPort.applyAdjustment(applyRequest);
        
        log.info("조정 내역 승인 완료. adjustmentRequestId: {}, approveUserId: {}", 
                request.getAdjustmentRequestId(), request.getAdjustmentApproveUserId());

        return adjustments.get(0).getVocId();
    }    

    @Transactional
    public String processAdjustmentCancelation(AdjustmentCancelRequest request) {
        List<Adjustment> adjustments;
        
        // 승인자 없이 완료한 경우와 승인자가 승인한 경우를 구분하여 조회
        if (request.getAdjustmentRequestId() != null && !request.getAdjustmentRequestId().isEmpty()) {
            // 승인자가 승인한 후에 취소하는 경우
            adjustments = adjustmentRepositoryPort.findByAdjustmentRequestId(request.getAdjustmentRequestId());
        } else {
            // 승인자 없이 완료한 뒤 취소하는 경우
            adjustments = adjustmentRepositoryPort.findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                    request.getServiceManagementNumber(),
                    request.getAccountNumber(),
                    request.getAdjustmentRequestDateTime()
            );
        }
        
        if (adjustments.isEmpty()) {
            throw new BusinessException("조정 내역을 찾을 수 없습니다.");
        }
        
        // 1. 조회된 Adjustment중 하나라도 adjustmentStatusCode가 '승인'이 아니면 예외 던짐
        boolean hasInvalidStatus = adjustments.stream()
                .anyMatch(adjustment -> adjustment.getAdjustmentStatusCode() != AdjustmentStatusCode.APPROVE);        
        if (hasInvalidStatus) {
            throw new BusinessException("승인 상태가 아닌 조정 내역이 포함되어 있습니다.");
        }
        
        // 2. 조회된 Adjustment들의 adjustmentStatusCode = '취소'로 변경
        adjustments.forEach(adjustment -> adjustment.cancel());
        
        // 3. 변경된 Adjustment 저장
        adjustmentRepositoryPort.saveAll(adjustments);
        
        // 4. invoiceCommandPort에 조정취소반영 api 호출
        ApplyAdjustmentRequestDto cancelRequest = buildApplyAdjustmentRequest(adjustments);        
        invoiceClientPort.cancelAdjustment(cancelRequest);
        
        log.info("조정 내역 취소 완료. adjustmentRequestId: {}", adjustments.get(0).getAdjustmentRequestId());

        return adjustments.get(0).getVocId();
    }    
    
    private ApplyAdjustmentRequestDto buildApplyAdjustmentRequest(List<Adjustment> adjustments) {
        if (adjustments.isEmpty()) {
            throw new BusinessException("조정 내역이 없습니다.");
        }
        
        Adjustment firstAdjustment = adjustments.get(0);
        
        List<AdjustmentItemDto> items = adjustments.stream()
                .map(adjustment -> AdjustmentItemDto.builder()
                        .revenueItemCode(adjustment.getRevenueItemCode())
                        .adjustmentRequestAmount(adjustment.getAdjustmentRequestAmount())
                        .build())
                .collect(Collectors.toList());
        
        return ApplyAdjustmentRequestDto.builder()
                .adjustmentRequestId(firstAdjustment.getAdjustmentRequestId())
                .serviceManagementNumber(firstAdjustment.getServiceManagementNumber())
                .accountNumber(firstAdjustment.getAccountNumber())
                .billingDate(firstAdjustment.getBillingDate().toString())
                .items(items)
                .build();
    }    
} 