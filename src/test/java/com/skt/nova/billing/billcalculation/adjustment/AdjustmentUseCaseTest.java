package com.skt.nova.billing.billcalculation.adjustment;

import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentApprovalRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentRejectionRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentCancelRequest;
import com.skt.nova.billing.billcalculation.adjustment.application.AdjustmentUseCase;
import com.skt.nova.billing.billcalculation.adjustment.application.AfterAdjustmentBusinessService;
import com.skt.nova.billing.billcalculation.adjustment.domain.Adjustment;
import com.skt.nova.billing.billcalculation.adjustment.domain.AdjustmentStatusCode;
import com.skt.nova.billing.billcalculation.adjustment.port.out.AdjustmentRepositoryPort;
import com.skt.nova.billing.billcalculation.adjustment.port.out.VocSystemPort;
import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AdjustmentAuthorizationPort;
import com.skt.nova.billing.billcalculation.adjustmentauthorization.api.AuthorizationResult;
import com.skt.nova.billing.billcalculation.approvalrequest.api.ApprovalRequestPort;
import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandPort;
import com.skt.nova.billing.billcalculation.adjustment.application.AdjustmentDomainDtoMapper;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentResponse;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentDto;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdjustmentUseCaseTest {
    @Mock
    private AdjustmentRepositoryPort adjustmentRepositoryPort;
    @Mock
    private AdjustmentDomainDtoMapper adjustmentMapper;
    @Mock
    private VocSystemPort vocSystemPort;
    @Mock
    private AdjustmentAuthorizationPort adjustmentAuthorizationPort;
    @Mock
    private ApprovalRequestPort approvalRequestPort;
    @Mock
    private InvoiceCommandPort invoiceCommandPort;

    private AdjustmentUseCase adjustmentUseCase;
    private AfterAdjustmentBusinessService afterAdjustmentBusinessService;

    @BeforeEach
    void setUp() {
        afterAdjustmentBusinessService = new AfterAdjustmentBusinessService(
                adjustmentAuthorizationPort, 
                approvalRequestPort, 
                invoiceCommandPort, 
                adjustmentRepositoryPort
        );
        adjustmentUseCase = new AdjustmentUseCase(
                adjustmentRepositoryPort, 
                adjustmentMapper, 
                afterAdjustmentBusinessService, 
                vocSystemPort
        );
    }

    @Test
    void 승인_정상_처리() {
        // given
        AdjustmentApprovalRequest request = AdjustmentApprovalRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentApproveUserId("APPROVER")
                .adjustmentApprovedDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createMultipleAdjustments("ADJ_REQ_001", AdjustmentStatusCode.APPROVE_REQUEST);
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(adjustments);
        
        // when
        adjustmentUseCase.approveAdjustment(request);
        
        // then
        verify(adjustmentRepositoryPort).findByAdjustmentRequestId("ADJ_REQ_001");
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort).applyAdjustment(any());
        verify(vocSystemPort).updateVocStatus(any());
    }

    @Test
    void 승인_조정내역_없음_예외() {
        // given
        AdjustmentApprovalRequest request = AdjustmentApprovalRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentApproveUserId("APPROVER")
                .adjustmentApprovedDateTime(LocalDateTime.now())
                .build();
        
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(List.of());
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.approveAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("조정 내역을 찾을 수 없습니다. adjustmentRequestId: ADJ_REQ_001");
    }

    @Test
    void 승인_승인요청상태가_아닌_조정내역_포함_예외() {
        // given
        AdjustmentApprovalRequest request = AdjustmentApprovalRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentApproveUserId("APPROVER")
                .adjustmentApprovedDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createAdjustmentsWithMixedStatus("ADJ_REQ_001");
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(adjustments);
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.approveAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("승인 요청 상태가 아닌 조정 내역이 포함되어 있습니다. adjustmentRequestId: ADJ_REQ_001");
    }

    @Test
    void 반려_정상_처리() {
        // given
        AdjustmentRejectionRequest request = AdjustmentRejectionRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentRejectUserId("REJECTOR")
                .adjustmentRejectedDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createMultipleAdjustments("ADJ_REQ_001", AdjustmentStatusCode.APPROVE_REQUEST);
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(adjustments);
        
        // when
        adjustmentUseCase.rejectAdjustment(request);
        
        // then
        verify(adjustmentRepositoryPort).findByAdjustmentRequestId("ADJ_REQ_001");
        verify(adjustmentRepositoryPort).saveAll(any());
    }

    @Test
    void 반려_조정내역_없음_예외() {
        // given
        AdjustmentRejectionRequest request = AdjustmentRejectionRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentRejectUserId("REJECTOR")
                .adjustmentRejectedDateTime(LocalDateTime.now())
                .build();
        
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(List.of());
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.rejectAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("조정 내역을 찾을 수 없습니다. adjustmentRequestId: ADJ_REQ_001");
    }

    @Test
    void 반려_승인요청상태가_아닌_조정내역_포함_예외() {
        // given
        AdjustmentRejectionRequest request = AdjustmentRejectionRequest.builder()
                .adjustmentRequestId("ADJ_REQ_001")
                .adjustmentRejectUserId("REJECTOR")
                .adjustmentRejectedDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createAdjustmentsWithMixedStatus("ADJ_REQ_001");
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("ADJ_REQ_001")).thenReturn(adjustments);
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.rejectAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("승인 요청 상태가 아닌 조정 내역이 포함되어 있습니다. adjustmentRequestId: ADJ_REQ_001");
    }

    @Test
    void 취소_승인자없이_완료한_경우_정상_처리() {
        // given
        AdjustmentCancelRequest request = AdjustmentCancelRequest.builder()
                .serviceManagementNumber("1234567890")
                .accountNumber("9876543210")
                .adjustmentRequestDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createMultipleAdjustments("ADJ_REQ_001", AdjustmentStatusCode.APPROVE);
        when(adjustmentRepositoryPort.findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                "1234567890", "9876543210", request.getAdjustmentRequestDateTime())).thenReturn(adjustments);
        
        // when
        adjustmentUseCase.cancelAdjustment(request);
        
        // then
        verify(adjustmentRepositoryPort).findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                "1234567890", "9876543210", request.getAdjustmentRequestDateTime());
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort).cancelAdjustment(any());
    }

    @Test
    void 취소_승인자가_승인한_경우_정상_처리() {
        // given
        AdjustmentCancelRequest request = AdjustmentCancelRequest.builder()
                .adjustmentRequestId("REQ_ID")
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createMultipleAdjustments("ADJ_REQ_001", AdjustmentStatusCode.APPROVE);
        when(adjustmentRepositoryPort.findByAdjustmentRequestId("REQ_ID")).thenReturn(adjustments);
        
        // when
        adjustmentUseCase.cancelAdjustment(request);
        
        // then
        verify(adjustmentRepositoryPort).findByAdjustmentRequestId("REQ_ID");
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort).cancelAdjustment(any());
    }

    @Test
    void 취소_조정내역_없음_예외() {
        // given
        AdjustmentCancelRequest request = AdjustmentCancelRequest.builder()
                .serviceManagementNumber("1234567890")
                .accountNumber("9876543210")
                .adjustmentRequestDateTime(LocalDateTime.now())
                .build();
        
        when(adjustmentRepositoryPort.findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                any(), any(), any())).thenReturn(List.of());
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.cancelAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("조정 내역을 찾을 수 없습니다.");
    }

    @Test
    void 취소_승인상태가_아닌_조정내역_포함_예외() {
        // given
        AdjustmentCancelRequest request = AdjustmentCancelRequest.builder()
                .serviceManagementNumber("1234567890")
                .accountNumber("9876543210")
                .adjustmentRequestDateTime(LocalDateTime.now())
                .build();
        
        List<Adjustment> adjustments = AdjustmentTestFixture.createAdjustmentsWithMixedStatus("ADJ_REQ_001");
        when(adjustmentRepositoryPort.findByServiceManagementNumberAndAccountNumberAndAdjustmentRequestDateTime(
                any(), any(), any())).thenReturn(adjustments);
        
        // when & then
        assertThatThrownBy(() -> adjustmentUseCase.cancelAdjustment(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("승인 상태가 아닌 조정 내역이 포함되어 있습니다.");
    }

    @Test
    void 후조정_요청_승인없이_완료_처리() {
        // given
        AfterAdjustmentRequest request = new AfterAdjustmentRequest();
        request.setServiceManagementNumber("1234567890");
        request.setAccountNumber("9876543210");
        request.setBillingDate("2024-01-31");
        request.setAdjustmentRequestDateTime("2024-01-31T10:00:00");
        request.setAdjustmentRequestUserId("REQUEST_USER");
        request.setAdjustmentReasonCode("BILLING_ERROR");
        request.setAdjustmentReasonPhrase("과금 오류");
        request.setVocId("VOC_001");
        AdjustmentItem item = new AdjustmentItem();
        item.setRevenueItemCode("REV001");
        item.setAdjustmentRequestAmount(-1000L);
        request.setItems(List.of(item));
        
        // 권한 체크에서 승인 가능한 경우로 설정
        when(adjustmentAuthorizationPort.checkAdjustmentAuthorization("REQUEST_USER", -1000L))
                .thenReturn(AuthorizationResult.APPROVE);
        
        // when
        AfterAdjustmentResponse response = adjustmentUseCase.requestAfterAdjustment(request);
        
        // then
        verify(adjustmentAuthorizationPort).checkAdjustmentAuthorization("REQUEST_USER", -1000L);
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort).applyAdjustment(any());
        verify(vocSystemPort).updateVocStatus(any());
        
        assertThat(response.getStatus()).isEqualTo("APPROVE");
        assertThat(response.getApprovalRequestId()).isNull();
    }

    @Test
    void 후조정_요청_승인_필요_처리() {
        // given
        AfterAdjustmentRequest request = new AfterAdjustmentRequest();
        request.setServiceManagementNumber("1234567890");
        request.setAccountNumber("9876543210");
        request.setBillingDate("2024-01-31");
        request.setAdjustmentRequestDateTime("2024-01-31T10:00:00");
        request.setAdjustmentRequestUserId("REQUEST_USER");
        request.setAdjustmentReasonCode("BILLING_ERROR");
        request.setAdjustmentReasonPhrase("과금 오류");
        request.setVocId("VOC_001");
        AdjustmentItem item = new AdjustmentItem();
        item.setRevenueItemCode("REV001");
        item.setAdjustmentRequestAmount(-10000L);
        request.setItems(List.of(item));
        
        // 권한 체크에서 승인 요청이 필요한 경우로 설정
        when(adjustmentAuthorizationPort.checkAdjustmentAuthorization("REQUEST_USER", -10000L))
                .thenReturn(AuthorizationResult.REQUEST_APPROVAL);
        when(approvalRequestPort.requestApproval("REQUEST_USER", -10000L))
                .thenReturn("APPROVAL_REQ_001");
        
        // when
        AfterAdjustmentResponse response = adjustmentUseCase.requestAfterAdjustment(request);
        
        // then
        verify(adjustmentAuthorizationPort).checkAdjustmentAuthorization("REQUEST_USER", -10000L);
        verify(approvalRequestPort).requestApproval("REQUEST_USER", -10000L);
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort, never()).applyAdjustment(any());
        verify(vocSystemPort, never()).updateVocStatus(any());
        
        assertThat(response.getStatus()).isEqualTo("APPROVE_REQUEST");
        assertThat(response.getApprovalRequestId()).isEqualTo("APPROVAL_REQ_001");
    }

    @Test
    void 후조정_요청_VocSystem_연동_실패시_예외_전파하지_않음() {
        // given
        AfterAdjustmentRequest request = new AfterAdjustmentRequest();
        request.setServiceManagementNumber("1234567890");
        request.setAccountNumber("9876543210");
        request.setBillingDate("2024-01-31");
        request.setAdjustmentRequestDateTime("2024-01-31T10:00:00");
        request.setAdjustmentRequestUserId("REQUEST_USER");
        request.setAdjustmentReasonCode("BILLING_ERROR");
        request.setAdjustmentReasonPhrase("과금 오류");
        request.setVocId("VOC_001");
        AdjustmentItem item = new AdjustmentItem();
        item.setRevenueItemCode("REV001");
        item.setAdjustmentRequestAmount(-1000L);
        request.setItems(List.of(item));
        
        when(adjustmentAuthorizationPort.checkAdjustmentAuthorization("REQUEST_USER", -1000L))
                .thenReturn(AuthorizationResult.APPROVE);
        doThrow(new RuntimeException("VocSystem 연동 실패")).when(vocSystemPort).updateVocStatus(any());
        
        // when & then - VocSystem 연동 실패는 예외를 전파하지 않아야 함
        AfterAdjustmentResponse response = adjustmentUseCase.requestAfterAdjustment(request);
        
        verify(adjustmentRepositoryPort).saveAll(any());
        verify(invoiceCommandPort).applyAdjustment(any());
        verify(vocSystemPort).updateVocStatus(any());
        
        assertThat(response.getStatus()).isEqualTo("APPROVE");
    }

    @Test
    void 조회_서비스관리번호로_조정내역_조회_정상_처리() {
        // given
        String serviceManagementNumber = "1234567890";
        List<Adjustment> adjustments = AdjustmentTestFixture.createMultipleAdjustments("ADJ_REQ_001", AdjustmentStatusCode.APPROVE);
        List<AdjustmentDto> expectedDtos = adjustments.stream()
                .map(adj -> AdjustmentDto.builder()
                        .serviceManagementNumber(adj.getServiceManagementNumber())
                        .adjustmentDate(adj.getAdjustmentDate())
                        .revenueItemCode(adj.getRevenueItemCode())
                        .adjustmentSequence(adj.getAdjustmentSequence())
                        .adjustmentRequestAmount(adj.getAdjustmentRequestAmount())
                        .adjustmentType(adj.getAdjustmentType())
                        .adjustmentStatusCode(adj.getAdjustmentStatusCode())
                        .adjustmentRequestDateTime(adj.getAdjustmentRequestDateTime())
                        .adjustmentReasonCode(adj.getAdjustmentReasonCode())
                        .adjustmentReasonPhrase(adj.getAdjustmentReasonPhrase())
                        .adjustmentApproveRequestUserId(adj.getAdjustmentApproveRequestUserId())
                        .adjustmentApproveUserId(adj.getAdjustmentApproveUserId())
                        .adjustmentApprovedDateTime(adj.getAdjustmentApprovedDateTime())
                        .accountNumber(adj.getAccountNumber())
                        .billingDate(adj.getBillingDate())
                        .adjustmentRequestId(adj.getAdjustmentRequestId())
                        .build())
                .collect(Collectors.toList());
        
        when(adjustmentRepositoryPort.findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber))
                .thenReturn(adjustments);
        when(adjustmentMapper.mapToDto(any(Adjustment.class)))
                .thenAnswer(invocation -> {
                    Adjustment adj = invocation.getArgument(0);
                    return AdjustmentDto.builder()
                            .serviceManagementNumber(adj.getServiceManagementNumber())
                            .adjustmentDate(adj.getAdjustmentDate())
                            .revenueItemCode(adj.getRevenueItemCode())
                            .adjustmentSequence(adj.getAdjustmentSequence())
                            .adjustmentRequestAmount(adj.getAdjustmentRequestAmount())
                            .adjustmentType(adj.getAdjustmentType())
                            .adjustmentStatusCode(adj.getAdjustmentStatusCode())
                            .adjustmentRequestDateTime(adj.getAdjustmentRequestDateTime())
                            .adjustmentReasonCode(adj.getAdjustmentReasonCode())
                            .adjustmentReasonPhrase(adj.getAdjustmentReasonPhrase())
                            .adjustmentApproveRequestUserId(adj.getAdjustmentApproveRequestUserId())
                            .adjustmentApproveUserId(adj.getAdjustmentApproveUserId())
                            .adjustmentApprovedDateTime(adj.getAdjustmentApprovedDateTime())
                            .accountNumber(adj.getAccountNumber())
                            .billingDate(adj.getBillingDate())
                            .adjustmentRequestId(adj.getAdjustmentRequestId())
                            .build();
                });
        
        // when
        List<AdjustmentDto> result = adjustmentUseCase.findByServiceManagementNumber(serviceManagementNumber);
        
        // then
        verify(adjustmentRepositoryPort).findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber);
        verify(adjustmentMapper, times(2)).mapToDto(any(Adjustment.class));
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getServiceManagementNumber()).isEqualTo(serviceManagementNumber);
        assertThat(result.get(1).getServiceManagementNumber()).isEqualTo(serviceManagementNumber);
    }

    @Test
    void 조회_서비스관리번호로_조정내역_없는_경우_빈_리스트_반환() {
        // given
        String serviceManagementNumber = "1234567890";
        when(adjustmentRepositoryPort.findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber))
                .thenReturn(List.of());
        
        // when
        List<AdjustmentDto> result = adjustmentUseCase.findByServiceManagementNumber(serviceManagementNumber);
        
        // then
        verify(adjustmentRepositoryPort).findByIdServiceManagementNumberOrderByIdAdjustmentDateDescIdAdjustmentSequenceAsc(serviceManagementNumber);
        verify(adjustmentMapper, never()).mapToDto(any(Adjustment.class));
        
        assertThat(result).isEmpty();
    }
} 