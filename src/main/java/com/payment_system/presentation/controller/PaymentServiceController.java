package com.payment_system.presentation.controller;

import com.payment_system.application.exception.DuplicatePaymentServiceNameException;
import com.payment_system.application.exception.InvalidBillAssociationException;
import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.application.service.PaymentServiceService;
import com.payment_system.domain.model.Bill.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-services/")
public class PaymentServiceController {

    @Autowired
    private PaymentServiceService paymentServiceService;

    @PostMapping("register")
    public ResponseEntity<Object> createPaymentService(@RequestBody PaymentService paymentService) {
        try {
            return ResponseEntity.ok(paymentServiceService.savePaymentService(paymentService));
        } catch (DuplicatePaymentServiceNameException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PaymentService> getPaymentService(@PathVariable UUID id) {
        return paymentServiceService.getPaymentServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("list")
    public ResponseEntity<Page<PaymentService>> getPaymentServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentService> paymentServices = paymentServiceService.getPaymentServices(pageable);
        return ResponseEntity.ok(paymentServices);
    }

    @PutMapping("{id}")
    public ResponseEntity<PaymentService> updatePaymentService(@PathVariable UUID id, @RequestBody PaymentService paymentService) {
        try {
            return ResponseEntity.ok(paymentServiceService.updatePaymentService(id, paymentService));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePaymentService(@PathVariable UUID id) {
        try {
            paymentServiceService.deletePaymentService(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("associate-bills/{id}/")
    public ResponseEntity<Object> associateBillsToPaymentService(
            @PathVariable UUID id,
            @RequestBody List<Map<String, UUID>> associateBills
    ) {
        List<UUID> billIds = associateBills.stream()
                .map(map -> map.get("id"))
                .collect(Collectors.toList());

        try {
            paymentServiceService.associateBillsToPaymentService(id, billIds);
            return ResponseEntity.ok().build();
        } catch (InvalidBillAssociationException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message", "Invalid bill association",
                            "notFoundBillIds", e.getNotFoundBillIds(),
                            "alreadyAssociatedBillIds", e.getAlreadyAssociatedBillIds()
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("search")
    public ResponseEntity<Page<PaymentService>> searchPaymentServices(
            @RequestParam String paymentServiceName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentService> paymentServices = paymentServiceService.searchPaymentServices(paymentServiceName, pageable);
        return ResponseEntity.ok(paymentServices);
    }
}
