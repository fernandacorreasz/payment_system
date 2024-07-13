package com.payment_system.application.service;

import com.payment_system.application.exception.DuplicatePaymentServiceNameException;
import com.payment_system.application.exception.InvalidBillAssociationException;
import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.PaymentService;
import com.payment_system.domain.repository.BillRepository;
import com.payment_system.domain.repository.PaymentServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceService {

    @Autowired
    private PaymentServiceRepository paymentServiceRepository;

    @Autowired
    private BillRepository billRepository;

    public PaymentService savePaymentService(PaymentService paymentService) {
        if (paymentServiceRepository.existsByPaymentServiceName(paymentService.getPaymentServiceName())) {
            throw new DuplicatePaymentServiceNameException("Payment service name already exists: " + paymentService.getPaymentServiceName());
        }
        return paymentServiceRepository.save(paymentService);
    }

    public Optional<PaymentService> getPaymentServiceById(UUID id) {
        return paymentServiceRepository.findById(id);
    }

    public Page<PaymentService> getPaymentServices(Pageable pageable) {
        return paymentServiceRepository.findAll(pageable);
    }

    public void deletePaymentService(UUID id) {
        if (!paymentServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment service with id " + id + " not found.");
        }
        paymentServiceRepository.deleteById(id);
    }

    public PaymentService updatePaymentService(UUID id, PaymentService paymentService) {
        if (!paymentServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment service with id " + id + " not found.");
        }
        paymentService.setId(id);
        return paymentServiceRepository.save(paymentService);
    }

    public void associateBillsToPaymentService(UUID paymentServiceId, List<UUID> billIds) {
        PaymentService paymentService = paymentServiceRepository.findById(paymentServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment service with id " + paymentServiceId + " not found."));

        List<Bill> bills = billRepository.findAllById(billIds);

        List<UUID> foundBillIds = bills.stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        List<UUID> notFoundBillIds = billIds.stream()
                .filter(billId -> !foundBillIds.contains(billId))
                .collect(Collectors.toList());

        List<UUID> alreadyAssociatedBillIds = bills.stream()
                .filter(bill -> bill.getPaymentService() != null)
                .map(Bill::getId)
                .collect(Collectors.toList());

        if (!notFoundBillIds.isEmpty() || !alreadyAssociatedBillIds.isEmpty()) {
            throw new InvalidBillAssociationException(notFoundBillIds, alreadyAssociatedBillIds);
        }

        bills.forEach(bill -> bill.setPaymentService(paymentService));
        billRepository.saveAll(bills);
    }

    public Page<PaymentService> searchPaymentServices(String paymentServiceName, Pageable pageable) {
        return paymentServiceRepository.findByPaymentServiceNameContaining(paymentServiceName, pageable);
    }
}
