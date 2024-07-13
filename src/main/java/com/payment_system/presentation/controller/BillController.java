package com.payment_system.presentation.controller;

import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.application.service.BillService;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.dto.BillDTO;
import com.payment_system.domain.model.Bill.dto.BillStatusDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bills/")
public class BillController {

    @Autowired
    private BillService billService;
    
    @PostMapping("register")
    public ResponseEntity<Object> createBill(@RequestBody Bill bill) {
        try {
            BillDTO savedBill = billService.saveBill(bill);
            return ResponseEntity.ok(savedBill);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Bill> getBill(@PathVariable UUID id) {
        return billService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("list")
    public ResponseEntity<Page<BillDTO>> getBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BillDTO> bills = billService.getBills(pageable);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("list-with-filters")
    public ResponseEntity<Page<BillDTO>> getBillsWithFilters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BillDTO> bills = billService.getBillsWithFilters(dueDate, description, pageable);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("total-paid-amount")
    public ResponseEntity<Object> getTotalPaidAmountByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            Double totalPaidAmount = billService.getTotalPaidAmountByPeriod(startDate, endDate);
            return ResponseEntity.ok(Map.of("totalPaidAmount", totalPaidAmount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable UUID id, @RequestBody Bill bill) {
        try {
            return ResponseEntity.ok(billService.updateBill(id, bill));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("status/{id}")
    public ResponseEntity<BillStatusDTO> updateBillStatus(@PathVariable UUID id, @RequestParam String status) {
        try {
            BillStatusDTO updatedStatus = billService.updateBillStatus(id, status);
            return ResponseEntity.ok(updatedStatus);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new BillStatusDTO(id, status, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable UUID id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
 
