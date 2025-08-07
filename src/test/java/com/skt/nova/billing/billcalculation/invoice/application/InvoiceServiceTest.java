package com.skt.nova.billing.billcalculation.invoice.application;

import com.skt.nova.billing.billcalculation.common.exception.BusinessException;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceCommandUseCase;
import com.skt.nova.billing.billcalculation.invoice.api.InvoiceQueryUseCase;
import com.skt.nova.billing.billcalculation.invoice.domain.InvoiceMaster;
import com.skt.nova.billing.billcalculation.invoice.api.dto.AdjustmentItemDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.ApplyAdjustmentRequestDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceMasterDto;
import com.skt.nova.billing.billcalculation.invoice.api.dto.InvoiceSummaryDto;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceCommandRepositoryPort;
import com.skt.nova.billing.billcalculation.invoice.port.out.InvoiceQueryRepositoryPort;
import com.skt.nova.billing.billcalculation.invoice.infrastructure.InvoiceMasterId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {
    
    @Mock
    private InvoiceCommandRepositoryPort invoiceCommandRepositoryPort;
    @Mock
    private InvoiceQueryRepositoryPort invoiceQueryRepositoryPort;
    
    private InvoiceCommandUseCase invoiceCommandUseCase;
    private InvoiceQueryUseCase invoiceQueryUseCase;

    private InvoiceMapper invoiceMapper = new InvoiceMapper();
    
    @BeforeEach
    void setUp() {
        invoiceCommandUseCase = new InvoiceCommandUseCaseImpl(invoiceCommandRepositoryPort, invoiceQueryRepositoryPort);
        invoiceQueryUseCase = new InvoiceQueryUseCaseImpl(invoiceQueryRepositoryPort, invoiceMapper);
    }
    
    @Test
    void applyAdjustment_정상적인_조정_요청_성공() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("-1000"))
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when
        invoiceCommandUseCase.applyAdjustment(requestDto);
        
        // then
        verify(invoiceQueryRepositoryPort).findById(id);
        verify(invoiceCommandRepositoryPort).save(any(InvoiceMaster.class));
    }
    
    @Test
    void applyAdjustment_청구정보를_찾을_수_없는_경우_예외발생() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("-1000"))
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> invoiceCommandUseCase.applyAdjustment(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("청구 정보를 찾을 수 없습니다.");
        
        verify(invoiceQueryRepositoryPort).findById(id);
    }
    
    @Test
    void applyAdjustment_총조정금액이_미납금액보다_큰_경우_예외발생() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("-10000")) // 미납금액(5000)보다 큰 음수
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when & then
        assertThatThrownBy(() -> invoiceCommandUseCase.applyAdjustment(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("총 조정 금액이 미납 금액보다 커서 전체 미납액을 음수로 만들 수 없습니다.");
        
        verify(invoiceQueryRepositoryPort).findById(id);
    }
    
    @Test
    void applyAdjustment_조정대상_매출항목이_존재하지_않는_경우_예외발생() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("NONEXISTENT") // 존재하지 않는 매출항목
                                .adjustmentRequestAmount(new BigDecimal("-1000"))
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when & then
        assertThatThrownBy(() -> invoiceCommandUseCase.applyAdjustment(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("조정 대상 매출 항목(NONEXISTENT)이 존재하지 않습니다.");
        
        verify(invoiceQueryRepositoryPort).findById(id);
    }
    
    @Test
    void applyAdjustment_조정금액이_음수이고_미납금액이_양수일때_합이_음수가_되는_경우_예외발생() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("-3000")) // 해당 항목의 미납금액(2000)보다 큰 음수
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when & then
        assertThatThrownBy(() -> invoiceCommandUseCase.applyAdjustment(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("조정 금액(-3000)이 미납 금액(2000)보다 커서 미납액을 음수로 만들 수 없습니다.");
        
        verify(invoiceQueryRepositoryPort).findById(id);
    }
    
    @Test
    void applyAdjustment_양수_조정금액_정상처리() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "9876543210";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("1000")) // 양수 조정
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when
        invoiceCommandUseCase.applyAdjustment(requestDto);
        
        // then
        verify(invoiceQueryRepositoryPort).findById(id);
        verify(invoiceCommandRepositoryPort).save(any(InvoiceMaster.class));
    }
    
    @Test
    void applyAdjustment_여러_항목_조정_정상처리() {
        // given
        String accountNumber = "1234567890";
        String billingDate = "2024-01-31";
        String serviceManagementNumber = "SVC001";
        
        ApplyAdjustmentRequestDto requestDto = ApplyAdjustmentRequestDto.builder()
                .accountNumber(accountNumber)
                .billingDate(billingDate)
                .serviceManagementNumber(serviceManagementNumber)
                .items(List.of(
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV001")
                                .adjustmentRequestAmount(new BigDecimal("-500"))
                                .build(),
                        AdjustmentItemDto.builder()
                                .revenueItemCode("REV002")
                                .adjustmentRequestAmount(new BigDecimal("-300"))
                                .build()
                ))
                .build();
        
        InvoiceMasterId id = new InvoiceMasterId(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        InvoiceMaster mockInvoiceMaster = InvoiceTestFixture.createMockInvoiceMasterWithMultipleDetails(accountNumber, LocalDate.parse(billingDate), serviceManagementNumber);
        
        when(invoiceQueryRepositoryPort.findById(id)).thenReturn(Optional.of(mockInvoiceMaster));
        
        // when
        invoiceCommandUseCase.applyAdjustment(requestDto);
        
        // then
        verify(invoiceQueryRepositoryPort).findById(id);
        verify(invoiceCommandRepositoryPort).save(any(InvoiceMaster.class));
    }
    
    @Test
    void findInvoiceSummaryByAccountNumber_ShouldReturnSummaries() {
        // given
        String accountNumber = "1234567890";
        List<InvoiceSummaryDto> expectedSummaries = InvoiceTestFixture.createMockSummaries(accountNumber);
        when(invoiceQueryRepositoryPort.findInvoiceSummaryByAccountNumber(accountNumber)).thenReturn(expectedSummaries);
        
        // when
        List<InvoiceSummaryDto> result = invoiceQueryUseCase.findInvoiceSummaryByAccountNumber(accountNumber);
        
        // then
        assertThat(result).isEqualTo(expectedSummaries);
        assertThat(result.size()).isEqualTo(2);
    }
    
    @Test
    void findInvoicesByAccountNumberAndBillingDate_ShouldReturnInvoices() {
        // given
        String accountNumber = "1234567890";
        LocalDate billingDate = LocalDate.of(2024, 1, 31);
        List<InvoiceMaster> expectedInvoices = InvoiceTestFixture.createMockInvoices(accountNumber, billingDate);
        when(invoiceQueryRepositoryPort.findInvoicesByAccountNumberAndBillingDate(accountNumber, billingDate))
                .thenReturn(expectedInvoices);

        // when
        List<InvoiceMasterDto> result = invoiceQueryUseCase.findInvoicesByAccountNumberAndBillingDate(accountNumber, billingDate);

        // then
        // InvoiceMasterDto로 변환하여 비교
        List<InvoiceMasterDto> expectedDtos = expectedInvoices.stream()
                .map(invoiceMapper::mapToDto)
                .toList();
        assertThat(result).isEqualTo(expectedDtos);
        assertThat(result.size()).isEqualTo(expectedDtos.size());
    }
    
} 