package com.payment_system.application.service;

import com.payment_system.application.exception.DuplicateCategoryNameException;
import com.payment_system.application.exception.InvalidBillAssociationException;
import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.Category;
import com.payment_system.domain.repository.BillRepository;
import com.payment_system.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BillRepository billRepository;

    public Category saveCategory(Category category) {
        if (categoryRepository.existsByNameCategory(category.getNameCategory())) {
            throw new DuplicateCategoryNameException("Category name already exists: " + category.getNameCategory());
        }
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(UUID id, Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

    public void associateBillsToCategory(UUID categoryId, List<UUID> billIds) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found."));

        List<Bill> bills = billRepository.findAllById(billIds);

        List<UUID> foundBillIds = bills.stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        List<UUID> notFoundBillIds = billIds.stream()
                .filter(billId -> !foundBillIds.contains(billId))
                .collect(Collectors.toList());

        List<UUID> alreadyAssociatedBillIds = bills.stream()
                .filter(bill -> bill.getCategory() != null)
                .map(Bill::getId)
                .collect(Collectors.toList());

        if (!notFoundBillIds.isEmpty() || !alreadyAssociatedBillIds.isEmpty()) {
            throw new InvalidBillAssociationException(notFoundBillIds, alreadyAssociatedBillIds);
        }

        bills.forEach(bill -> bill.setCategory(category));
        billRepository.saveAll(bills);
    }
}
