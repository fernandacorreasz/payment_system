package com.payment_system.application.service;

import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.dto.BillDTO;
import com.payment_system.domain.model.Bill.dto.BillStatusDTO;
import com.payment_system.domain.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public BillDTO saveBill(Bill bill) {
        Bill savedBill = billRepository.save(bill);
        return convertToDTO(savedBill);
    }

    public Optional<Bill> getBillById(UUID id) {
        return billRepository.findById(id);
    }

    public Page<BillDTO> getBills(Pageable pageable) {
        return billRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<BillDTO> getBillsWithFilters(Date dueDate, String description, Pageable pageable) {
        if (dueDate != null && description != null) {
            return billRepository.findByDueDateAndDescriptionContaining(dueDate, description, pageable).map(this::convertToDTO);
        } else if (dueDate != null) {
            return billRepository.findByDueDate(dueDate, pageable).map(this::convertToDTO);
        } else if (description != null) {
            return billRepository.findByDescriptionContaining(description, pageable).map(this::convertToDTO);
        } else {
            return billRepository.findAll(pageable).map(this::convertToDTO);
        }
    }

    public Double getTotalPaidAmountByPeriod(Date startDate, Date endDate) {
        Double totalPaidAmount = billRepository.findTotalPaidAmountByPeriod(startDate, endDate);
        if (totalPaidAmount == null) {
            totalPaidAmount = 0.0;
        }
        return totalPaidAmount;
    }

    public void deleteBill(UUID id) {
        if (!billRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found.");
        }
        billRepository.deleteById(id);
    }

    public Bill updateBill(UUID id, Bill bill) {
        if (!billRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found.");
        }
        bill.setId(id);
        return billRepository.save(bill);
    }

    public BillStatusDTO updateBillStatus(UUID id, String status) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found."));
        bill.setStatus(status);
        billRepository.save(bill);
        return new BillStatusDTO(bill.getId(), bill.getStatus(), "Status updated successfully.");
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        billDTO.setId(bill.getId());
        billDTO.setDueDate(bill.getDueDate());
        billDTO.setPaymentDate(bill.getPaymentDate());
        billDTO.setAmount(bill.getAmount());
        billDTO.setDescription(bill.getDescription());
        billDTO.setStatus(bill.getStatus());
        return billDTO;
    }

    
    
}
