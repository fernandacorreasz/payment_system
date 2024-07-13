package com.payment_system.domain.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.payment_system.domain.model.Bill.Bill;


public interface BillRepository extends JpaRepository<Bill, UUID> {
   
}
