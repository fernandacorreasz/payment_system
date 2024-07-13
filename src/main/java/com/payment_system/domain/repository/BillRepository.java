package com.payment_system.domain.repository;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payment_system.domain.model.Bill.Bill;


public interface BillRepository extends JpaRepository<Bill, UUID> {
    Page<Bill> findByDueDateContainingAndDescriptionContaining(String dueDate, String description, Pageable pageable);
    Page<Bill> findByDescriptionContaining(String description, Pageable pageable);
    Page<Bill> findByDueDate(Date dueDate, Pageable pageable);
    Page<Bill> findByDueDateAndDescriptionContaining(Date dueDate, String description, Pageable pageable);

    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.paymentDate BETWEEN :startDate AND :endDate AND b.status = 'Paid'")
    Double findTotalPaidAmountByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
