package com.payment_system.domain.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.payment_system.domain.model.Bill.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentServiceRepository extends JpaRepository<PaymentService, UUID> {
    boolean existsByPaymentServiceName(String paymentServiceName);
    Page<PaymentService> findByPaymentServiceNameContaining(String paymentServiceName, Pageable pageable);
}
