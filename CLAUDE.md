# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build and Run
```bash
./gradlew build                    # Build the project
./gradlew bootRun                  # Run the application
./gradlew test                     # Run all tests
./gradlew test --tests ClassName   # Run specific test class
```

### Development Tools
```bash
./gradlew clean                    # Clean build artifacts
./gradlew compileQuerydsl          # Generate QueryDSL Q-classes
```

### Database Access
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`, Password: (empty)

## Current Project Status (Updated: 2025-09-04)

- **Total Java Source Files**: 105
- **Test Files**: 4 test classes
- **Recent Commits**: 
  - `40b2f54` - command,query분리 (Command/Query separation implemented)
  - `bc310b4` - 1 (Initial commit)
- **Build Status**: Clean (no uncommitted changes)

## Architecture Overview

This is a Spring Boot 3.5.3 application implementing a **Hexagonal Architecture** for a telecommunications billing system. The project uses **Java 21** and follows **Domain-Driven Design** principles.

### Core Business Domains

1. **Adjustment** (`adjustment/`) - Handles billing adjustments and VOC (Voice of Customer) requests
2. **Invoice** (`invoice/`) - Manages billing invoices and payment applications  
3. **Payment** (`payment/`) - Processes customer payments and refunds
4. **AdjustmentAuthorization** (`adjustmentauthorization/`) - Authorization for adjustments
5. **ApprovalRequest** (`approvalrequest/`) - Approval workflow management
6. **Common** (`common/`) - Shared utilities and components

### Architecture Layers

Each domain follows a consistent hexagonal architecture pattern:

- **`adapter/`** - Inbound (controllers) and outbound (external system clients) adapters
  - `in/` - REST controllers and input adapters
  - `out/` - Database repositories and external service clients
- **`api/`** - Port interfaces and DTOs for external communication
- **`application/`** - Use cases and application services (implements command/query ports)
- **`domain/`** - Core business logic and entities
- **`infrastructure/`** - JPA entities, repositories, and technical configuration
- **`port/out/`** - Outbound port interfaces

### Key Technology Stack

- **Spring Boot 3.5.3** with Spring Data JPA
- **QueryDSL 5.0.0** for type-safe queries (configured with Jakarta)
- **H2 Database** (in-memory for development)
- **Lombok** for reducing boilerplate
- **JUnit 5** for testing

### Important Integration Points

- **VOC System Integration**: The adjustment domain integrates with an external VOC (Voice of Customer) system for status updates
- **Cross-Domain Communication**: Domains communicate through well-defined ports and adapters (e.g., `InvoiceClientPort` in payment domain)

### Development Notes

- QueryDSL Q-classes are generated to `build/generated/querydsl` during compilation
- All domains use composite primary keys following the pattern `{Domain}Id` classes
- Transaction management is handled at the use case level with appropriate `@Transactional` annotations
- External service failures are handled gracefully without affecting core business operations
- **Recent Architecture Update**: Command/Query separation pattern implemented (CQRS)
- Main application class: `BillCalculationApplication.java`
- Package structure: `com.skt.nova.billing.billcalculation`

### Testing Structure

- Test fixtures are provided in `{Domain}TestFixture.java` files
- Unit tests focus on use case classes (`{Domain}UseCaseTest.java`)
- Integration tests can be run against the H2 in-memory database
- **Current Test Coverage**: Limited (4 test classes for 105 source files)

### Domain Structure Analysis

Each of the main domains (Adjustment, Invoice, Payment) follows consistent hexagonal architecture:

```
domain/
├── adapter/
│   ├── in/          # REST controllers  
│   └── out/         # Repository adapters, external clients
├── api/
│   └── dto/         # Data Transfer Objects
├── application/     # Use cases and application services
├── domain/          # Core business logic and entities
├── infrastructure/  # JPA entities and repositories
└── port/
    └── out/         # Outbound port interfaces
```

### Integration Points Identified

- **VocSystemAdapter**: Integration with Voice of Customer system
- **ApprovalClientAdapter**: Approval workflow integration  
- **InvoiceClientAdapter**: Cross-domain invoice communication
- **AdjustmentAuthorizationClientAdapter**: Authorization services