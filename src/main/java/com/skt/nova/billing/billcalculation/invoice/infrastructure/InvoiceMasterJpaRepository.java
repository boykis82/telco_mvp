package com.skt.nova.billing.billcalculation.invoice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceMasterJpaRepository extends JpaRepository<InvoiceMasterJpaEntity, InvoiceMasterId> {
} 