package com.skt.nova.billing.billcalculation.adjustment.application;

import com.skt.nova.billing.billcalculation.adjustment.api.AdjustmentCommandUseCase;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentResponse;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentApprovalRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentRejectionRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentCancelRequest;
import com.skt.nova.billing.billcalculation.adjustment.port.out.VocSystemPort;
import com.skt.nova.billing.billcalculation.adjustment.port.out.VocStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdjustmentCommandUseCaseImpl implements AdjustmentCommandUseCase {
    private final AfterAdjustmentBusinessService afterAdjustmentBusinessService;
    private final VocSystemPort vocSystemPort;

    @Override
    @Transactional
    public AfterAdjustmentResponse requestAfterAdjustment(AfterAdjustmentRequest request) {
        AfterAdjustmentResponse response = afterAdjustmentBusinessService.processAfterAdjustment(request);

        // 트랜잭션 완료 후 VocSystem 상태 업데이트 (비동기 처리)
        if (response.getStatus().equals(AdjustmentStatusCode.APPROVE.name())) {
            try {
                VocStatusUpdateRequest vocRequest = VocStatusUpdateRequest.builder()
                        .vocId(request.getVocId())
                        .statusCode("승인")
                        .build();
                
                vocSystemPort.updateVocStatus(vocRequest);
            } catch (Exception e) {
                log.error("VocSystem 상태 업데이트 실패: vocId={}, statusCode={}", request.getVocId(), response.getStatus(), e);
                // VocSystem 연동 실패는 후조정 처리에 영향을 주지 않도록 함
            }
        }

        return response;     

    }

    @Override
    public void approveAdjustment(AdjustmentApprovalRequest request) {
        String vocId = afterAdjustmentBusinessService.processAdjustmentApproval(request);
        try {
            VocStatusUpdateRequest vocRequest = VocStatusUpdateRequest.builder()
                    .vocId(vocId)
                    .statusCode("승인")
                    .build();
            
            vocSystemPort.updateVocStatus(vocRequest);
        } catch (Exception e) {
            log.error("VocSystem 상태 업데이트 실패: vocId={}, statusCode={}", vocId, e);
            // VocSystem 연동 실패는 후조정 처리에 영향을 주지 않도록 함
        }        
    }

    @Override
    @Transactional
    public void rejectAdjustment(AdjustmentRejectionRequest request) {
        String vocId = afterAdjustmentBusinessService.processAdjustmentRejection(request);
        try {
            VocStatusUpdateRequest vocRequest = VocStatusUpdateRequest.builder()
                    .vocId(vocId)
                    .statusCode("반려")
                    .build();
            
            vocSystemPort.updateVocStatus(vocRequest);
        } catch (Exception e) {
            log.error("VocSystem 상태 업데이트 실패: vocId={}", vocId, e);
            // VocSystem 연동 실패는 후조정 처리에 영향을 주지 않도록 함
        }       
    }

    @Override
    public void cancelAdjustment(AdjustmentCancelRequest request) {
        String vocId = afterAdjustmentBusinessService.processAdjustmentCancelation(request);
        try {
            VocStatusUpdateRequest vocRequest = VocStatusUpdateRequest.builder()
                    .vocId(vocId)
                    .statusCode("취소")
                    .build();
            
            vocSystemPort.updateVocStatus(vocRequest);
        } catch (Exception e) {
            log.error("VocSystem 상태 업데이트 실패: vocId={}, statusCode={}", vocId, e);
            // VocSystem 연동 실패는 후조정 처리에 영향을 주지 않도록 함
        }              
    }

} 