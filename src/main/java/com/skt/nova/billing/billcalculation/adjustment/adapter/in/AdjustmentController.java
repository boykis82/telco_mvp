package com.skt.nova.billing.billcalculation.adjustment.adapter.in;

import com.skt.nova.billing.billcalculation.adjustment.api.AdjustmentCommandPort;
import com.skt.nova.billing.billcalculation.adjustment.api.AdjustmentQueryPort;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AfterAdjustmentResponse;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentApprovalRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentRejectionRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentCancelRequest;
import com.skt.nova.billing.billcalculation.adjustment.api.dto.AdjustmentDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/adjustments")
@RequiredArgsConstructor
public class AdjustmentController {
    private final AdjustmentCommandPort adjustmentCommandPort;
    private final AdjustmentQueryPort adjustmentQueryPort;
    
    @PostMapping("/after")
    public ResponseEntity<AfterAdjustmentResponse> requestAfterAdjustment(@RequestBody AfterAdjustmentRequest request) {
        AfterAdjustmentResponse response = adjustmentCommandPort.requestAfterAdjustment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceManagementNumber}")
    public ResponseEntity<List<AdjustmentDto>> findByServiceManagementNumber(@PathVariable String serviceManagementNumber) {
        List<AdjustmentDto> response = adjustmentQueryPort.findByServiceManagementNumber(serviceManagementNumber);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/approve")
    public ResponseEntity<Void> approveAdjustment(@RequestBody AdjustmentApprovalRequest request) {
        adjustmentCommandPort.approveAdjustment(request);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectAdjustment(@RequestBody AdjustmentRejectionRequest request) {
        adjustmentCommandPort.rejectAdjustment(request);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelAdjustment(@RequestBody AdjustmentCancelRequest request) {
        adjustmentCommandPort.cancelAdjustment(request);
        return ResponseEntity.ok().build();
    }
} 