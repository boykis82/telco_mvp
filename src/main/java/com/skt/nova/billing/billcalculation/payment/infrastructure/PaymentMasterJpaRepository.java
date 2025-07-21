package com.skt.nova.billing.billcalculation.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMasterJpaRepository extends JpaRepository<PaymentMasterJpaEntity, PaymentMasterId> {
} 