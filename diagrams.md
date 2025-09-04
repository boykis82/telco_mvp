# Telco Billing System - Mermaid Diagrams

## 1. Overall System Class Diagram

```mermaid
classDiagram
    class AdjustmentController {
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +findByServiceManagementNumber(String) List~AdjustmentDto~
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class AdjustmentCommandUseCase {
        <<interface>>
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class AdjustmentQueryUseCase {
        <<interface>>
        +findByServiceManagementNumber(String) List~AdjustmentDto~
    }
    
    class AdjustmentCommandUseCaseImpl {
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class AdjustmentJpaEntity {
        -AdjustmentId id
        -BigDecimal adjustmentRequestAmount
        -AdjustmentType adjustmentType
        -AdjustmentStatusCode adjustmentStatusCode
        -LocalDateTime adjustmentRequestDateTime
        -AdjustmentReasonCode adjustmentReasonCode
        -String adjustmentReasonPhrase
        -String adjustmentApproveRequestUserId
        -String adjustmentApproveUserId
        -LocalDateTime adjustmentApprovedDateTime
        -String accountNumber
        -LocalDate billingDate
        -String adjustmentRequestId
    }
    
    class InvoiceController {
        +findInvoices() List~InvoiceDto~
        +createInvoice(InvoiceRequest) InvoiceResponse
    }
    
    class InvoiceMasterJpaEntity {
        -String accountNumber
        -LocalDate billingDate
        -String serviceManagementNumber
        -BigDecimal totalBillingAmount
        -BigDecimal billingAmount
        -BigDecimal unpaidAmount
        -BigDecimal adjustmentAmount
        -UnpaidStatusCode unpaidStatusCode
        -List~InvoiceDetailJpaEntity~ invoiceDetails
        -boolean fullyPaidAtOnce
    }
    
    class PaymentController {
        +processPayment(PaymentRequest) PaymentResponse
        +findPayments() List~PaymentDto~
    }
    
    class PaymentMasterJpaEntity {
        -String accountNumber
        -String serviceManagementNumber
        -LocalDateTime paymentDateTime
        -LocalDate billingDate
        -PaymentClassificationCode paymentClassificationCode
        -BigDecimal paymentAmount
        -List~PaymentDetailJpaEntity~ paymentDetails
        +addPaymentDetail(PaymentDetailJpaEntity) void
        +addPaymentDetails(List~PaymentDetailJpaEntity~) void
    }
    
    class VocSystemPort {
        <<interface>>
        +updateVocStatus(VocStatusUpdateRequest) void
    }
    
    class InvoiceClientPort {
        <<interface>>
        +updateInvoiceAmount(String, BigDecimal) void
    }
    
    class ApprovalClientPort {
        <<interface>>
        +requestApproval(ApprovalRequest) void
    }

    %% Relationships
    AdjustmentController --> AdjustmentCommandUseCase
    AdjustmentController --> AdjustmentQueryUseCase
    AdjustmentCommandUseCaseImpl ..|> AdjustmentCommandUseCase
    AdjustmentCommandUseCaseImpl --> VocSystemPort
    AdjustmentCommandUseCaseImpl --> ApprovalClientPort
    
    InvoiceController --> InvoiceCommandUseCase
    InvoiceController --> InvoiceQueryUseCase
    
    PaymentController --> PaymentCommandUseCase
    PaymentController --> PaymentQueryUseCase
    
    InvoiceMasterJpaEntity --> InvoiceDetailJpaEntity : "one-to-many"
    PaymentMasterJpaEntity --> PaymentDetailJpaEntity : "one-to-many"
```

## 2. Adjustment Domain - Detailed Class Diagram

```mermaid
classDiagram
    class AdjustmentController {
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class VocSystemAdapter {
        +updateVocStatus(VocStatusUpdateRequest) void
    }
    
    class ApprovalClientAdapter {
        +requestApproval(ApprovalRequest) void
    }
    
    class InvoiceClientAdapter {
        +updateInvoiceAmount(String, BigDecimal) void
    }
    
    class AdjustmentCommandUseCaseImpl {
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class AdjustmentQueryUseCaseImpl {
        +findByServiceManagementNumber(String) List~AdjustmentDto~
    }
    
    class AfterAdjustmentBusinessService {
        +processAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +processAdjustmentApproval(AdjustmentApprovalRequest) String
        +processAdjustmentRejection(AdjustmentRejectionRequest) String
        +processAdjustmentCancelation(AdjustmentCancelRequest) String
    }
    
    class AdjustmentStatusCode {
        <<enumeration>>
        PENDING
        APPROVED
        REJECTED
        CANCELLED
    }
    
    class AdjustmentType {
        <<enumeration>>
        DISCOUNT
        REFUND
        WAIVER
    }
    
    class AdjustmentReasonCode {
        <<enumeration>>
        SYSTEM_ERROR
        CUSTOMER_COMPLAINT
        BILLING_ERROR
    }
    
    class AdjustmentJpaEntity {
        -AdjustmentId id
        -BigDecimal adjustmentRequestAmount
        -AdjustmentType adjustmentType
        -AdjustmentStatusCode adjustmentStatusCode
        -LocalDateTime adjustmentRequestDateTime
    }
    
    class AdjustmentId {
        -String serviceManagementNumber
        -String adjustmentRequestId
    }
    
    class AdjustmentCommandUseCase {
        <<interface>>
        +requestAfterAdjustment(AfterAdjustmentRequest) AfterAdjustmentResponse
        +approveAdjustment(AdjustmentApprovalRequest) void
        +rejectAdjustment(AdjustmentRejectionRequest) void
        +cancelAdjustment(AdjustmentCancelRequest) void
    }
    
    class AdjustmentQueryUseCase {
        <<interface>>
        +findByServiceManagementNumber(String) List~AdjustmentDto~
    }
    
    class VocSystemPort {
        <<interface>>
        +updateVocStatus(VocStatusUpdateRequest) void
    }
    
    class ApprovalClientPort {
        <<interface>>
        +requestApproval(ApprovalRequest) void
    }
    
    class InvoiceClientPort {
        <<interface>>
        +updateInvoiceAmount(String, BigDecimal) void
    }
    
    %% Port implementations
    AdjustmentCommandUseCaseImpl ..|> AdjustmentCommandUseCase
    AdjustmentQueryUseCaseImpl ..|> AdjustmentQueryUseCase
    VocSystemAdapter ..|> VocSystemPort
    ApprovalClientAdapter ..|> ApprovalClientPort
    InvoiceClientAdapter ..|> InvoiceClientPort
    
    %% Dependencies
    AdjustmentController --> AdjustmentCommandUseCase
    AdjustmentController --> AdjustmentQueryUseCase
    AdjustmentCommandUseCaseImpl --> AfterAdjustmentBusinessService
    AdjustmentCommandUseCaseImpl --> VocSystemPort
    
    %% Domain relationships
    AdjustmentJpaEntity --> AdjustmentStatusCode
    AdjustmentJpaEntity --> AdjustmentType
    AdjustmentJpaEntity --> AdjustmentReasonCode
    AdjustmentJpaEntity --> AdjustmentId
```

## 3. Invoice Domain - Class Diagram

```mermaid
classDiagram
    class InvoiceController {
        +findInvoices() List~InvoiceDto~
        +createInvoice(InvoiceRequest) InvoiceResponse
    }
    
    class InvoiceCommandUseCase {
        <<interface>>
        +createInvoice(InvoiceRequest) InvoiceResponse
        +updateInvoice(InvoiceUpdateRequest) void
    }
    
    class InvoiceQueryUseCase {
        <<interface>>
        +findInvoices() List~InvoiceDto~
        +findInvoiceById(InvoiceId) InvoiceDto
    }
    
    class InvoiceMasterJpaEntity {
        -String accountNumber
        -LocalDate billingDate
        -String serviceManagementNumber
        -BigDecimal totalBillingAmount
        -BigDecimal billingAmount
        -BigDecimal unpaidAmount
        -BigDecimal adjustmentAmount
        -UnpaidStatusCode unpaidStatusCode
        -List~InvoiceDetailJpaEntity~ invoiceDetails
        -boolean fullyPaidAtOnce
    }
    
    class InvoiceDetailJpaEntity {
        -InvoiceMasterJpaEntity invoiceMaster
        -String chargeCode
        -BigDecimal chargeAmount
        -String description
    }
    
    class UnpaidStatusCode {
        <<enumeration>>
        PAID
        UNPAID
        PARTIAL_PAID
    }
    
    class InvoiceRepositoryPort {
        <<interface>>
        +save(InvoiceMasterJpaEntity) InvoiceMasterJpaEntity
        +findById(InvoiceMasterId) InvoiceMasterJpaEntity
    }

    %% Relationships
    InvoiceController --> InvoiceCommandUseCase
    InvoiceController --> InvoiceQueryUseCase
    InvoiceMasterJpaEntity --> InvoiceDetailJpaEntity : "one-to-many"
    InvoiceMasterJpaEntity --> UnpaidStatusCode
    InvoiceDetailJpaEntity --> InvoiceMasterJpaEntity
```

## 4. Payment Domain - Class Diagram

```mermaid
classDiagram
    class PaymentController {
        +processPayment(PaymentRequest) PaymentResponse
        +findPayments() List~PaymentDto~
    }
    
    class PaymentCommandUseCase {
        <<interface>>
        +processPayment(PaymentRequest) PaymentResponse
    }
    
    class PaymentQueryUseCase {
        <<interface>>
        +findPayments() List~PaymentDto~
        +findPaymentById(PaymentId) PaymentDto
    }
    
    class PaymentMasterJpaEntity {
        -String accountNumber
        -String serviceManagementNumber
        -LocalDateTime paymentDateTime
        -LocalDate billingDate
        -PaymentClassificationCode paymentClassificationCode
        -BigDecimal paymentAmount
        -List~PaymentDetailJpaEntity~ paymentDetails
        +addPaymentDetail(PaymentDetailJpaEntity) void
        +addPaymentDetails(List~PaymentDetailJpaEntity~) void
    }
    
    class PaymentDetailJpaEntity {
        -PaymentMasterJpaEntity paymentMaster
        -String paymentMethodCode
        -BigDecimal amount
        -String description
    }
    
    class PaymentClassificationCode {
        <<enumeration>>
        FULL_PAYMENT
        PARTIAL_PAYMENT
        REFUND
    }
    
    class InvoiceClientPort {
        <<interface>>
        +updateInvoiceAmount(String, BigDecimal) void
    }

    %% Relationships
    PaymentController --> PaymentCommandUseCase
    PaymentController --> PaymentQueryUseCase
    PaymentMasterJpaEntity --> PaymentDetailJpaEntity : "one-to-many"
    PaymentMasterJpaEntity --> PaymentClassificationCode
    PaymentDetailJpaEntity --> PaymentMasterJpaEntity
```

## 5. Adjustment Request Process - Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant AdjustmentController
    participant AdjustmentCommandUseCase
    participant AfterAdjustmentBusinessService
    participant VocSystemPort
    participant AdjustmentRepository
    
    Client->>+AdjustmentController: POST /api/v1/adjustments/after
    AdjustmentController->>+AdjustmentCommandUseCase: requestAfterAdjustment(request)
    
    AdjustmentCommandUseCase->>+AfterAdjustmentBusinessService: processAfterAdjustment(request)
    AfterAdjustmentBusinessService->>+AdjustmentRepository: save(adjustmentEntity)
    AdjustmentRepository-->>-AfterAdjustmentBusinessService: saved entity
    AfterAdjustmentBusinessService-->>-AdjustmentCommandUseCase: response
    
    alt response.status == APPROVE
        AdjustmentCommandUseCase->>+VocSystemPort: updateVocStatus(vocRequest)
        Note over VocSystemPort: Async VOC status update
        VocSystemPort-->>-AdjustmentCommandUseCase: status updated
    else VOC update fails
        Note over AdjustmentCommandUseCase: Log error but continue
    end
    
    AdjustmentCommandUseCase-->>-AdjustmentController: response
    AdjustmentController-->>-Client: HTTP 200 OK + response
```

## 6. Adjustment Approval Workflow - Sequence Diagram

```mermaid
sequenceDiagram
    participant Admin
    participant AdjustmentController
    participant AdjustmentCommandUseCase
    participant AfterAdjustmentBusinessService
    participant VocSystemPort
    participant AdjustmentRepository
    
    Admin->>+AdjustmentController: POST /api/v1/adjustments/approve
    AdjustmentController->>+AdjustmentCommandUseCase: approveAdjustment(request)
    
    AdjustmentCommandUseCase->>+AfterAdjustmentBusinessService: processAdjustmentApproval(request)
    AfterAdjustmentBusinessService->>+AdjustmentRepository: findById(adjustmentId)
    AdjustmentRepository-->>-AfterAdjustmentBusinessService: adjustment entity
    
    AfterAdjustmentBusinessService->>AfterAdjustmentBusinessService: updateStatusToApproved()
    AfterAdjustmentBusinessService->>+AdjustmentRepository: save(updatedEntity)
    AdjustmentRepository-->>-AfterAdjustmentBusinessService: saved entity
    AfterAdjustmentBusinessService-->>-AdjustmentCommandUseCase: vocId
    
    AdjustmentCommandUseCase->>+VocSystemPort: updateVocStatus("승인")
    Note over VocSystemPort: Update external VOC system
    VocSystemPort-->>-AdjustmentCommandUseCase: success
    
    AdjustmentCommandUseCase-->>-AdjustmentController: void
    AdjustmentController-->>-Admin: HTTP 200 OK
```

## 7. Cross-Domain Payment Processing - Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant PaymentController
    participant PaymentCommandUseCase
    participant PaymentRepository
    participant InvoiceClientPort
    participant InvoiceService
    
    Client->>+PaymentController: POST /api/v1/payments
    PaymentController->>+PaymentCommandUseCase: processPayment(request)
    
    PaymentCommandUseCase->>+PaymentRepository: save(paymentEntity)
    PaymentRepository-->>-PaymentCommandUseCase: saved payment
    
    Note over PaymentCommandUseCase: Update invoice with payment
    PaymentCommandUseCase->>+InvoiceClientPort: updateInvoiceAmount(accountNumber, amount)
    InvoiceClientPort->>+InvoiceService: updateUnpaidAmount(accountNumber, amount)
    InvoiceService->>InvoiceService: recalculateUnpaidAmount()
    InvoiceService-->>-InvoiceClientPort: updated invoice
    InvoiceClientPort-->>-PaymentCommandUseCase: success
    
    PaymentCommandUseCase-->>-PaymentController: payment response
    PaymentController-->>-Client: HTTP 200 OK + response
```

## 8. System Architecture Overview

```mermaid
graph TB
    subgraph "Presentation Layer"
        AC[AdjustmentController]
        IC[InvoiceController] 
        PC[PaymentController]
    end
    
    subgraph "Application Layer"
        ACU[AdjustmentCommandUseCase]
        AQU[AdjustmentQueryUseCase]
        ICU[InvoiceCommandUseCase]
        IQU[InvoiceQueryUseCase]
        PCU[PaymentCommandUseCase]
        PQU[PaymentQueryUseCase]
    end
    
    subgraph "Domain Layer"
        AE[Adjustment Entities]
        IE[Invoice Entities]
        PE[Payment Entities]
    end
    
    subgraph "Infrastructure Layer"
        AJE[AdjustmentJpaEntity]
        IJE[InvoiceMasterJpaEntity]
        PJE[PaymentMasterJpaEntity]
        H2[(H2 Database)]
    end
    
    subgraph "External Systems"
        VOC[VOC System]
        APPROVAL[Approval System]
    end
    
    %% Connections
    AC --> ACU
    AC --> AQU
    IC --> ICU
    IC --> IQU
    PC --> PCU
    PC --> PQU
    
    ACU --> AE
    AQU --> AE
    ICU --> IE
    IQU --> IE
    PCU --> PE
    PQU --> PE
    
    AE --> AJE
    IE --> IJE
    PE --> PJE
    
    AJE --> H2
    IJE --> H2
    PJE --> H2
    
    ACU -.-> VOC
    ACU -.-> APPROVAL
    PCU -.-> ICU
```

## 9. CQRS Pattern Implementation

```mermaid
graph LR
    subgraph "Command Side"
        CC[Command Controllers]
        CCU[Command Use Cases]
        CRP[Command Repository Ports]
    end
    
    subgraph "Query Side"
        QC[Query Controllers]
        QCU[Query Use Cases]
        QRP[Query Repository Ports]
    end
    
    subgraph "Shared"
        DB[(Database)]
        DE[Domain Entities]
    end
    
    CC --> CCU
    CCU --> CRP
    CRP --> DB
    
    QC --> QCU
    QCU --> QRP
    QRP --> DB
    
    CCU --> DE
    QCU --> DE
    DE --> DB
    
    style CC fill:#ffebee
    style CCU fill:#ffebee
    style CRP fill:#ffebee
    
    style QC fill:#e3f2fd
    style QCU fill:#e3f2fd
    style QRP fill:#e3f2fd
```