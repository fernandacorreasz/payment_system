package com.payment_system.presentation.controller;

import com.payment_system.application.exception.DuplicateCategoryNameException;
import com.payment_system.application.exception.InvalidBillAssociationException;
import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.application.service.CategoryService;
import com.payment_system.domain.model.Bill.Category;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("register")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.saveCategory(category));
        } catch (DuplicateCategoryNameException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable UUID id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("list")
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryService.getCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.updateCategory(id, category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("associate-bills/{id}")
    public ResponseEntity<Object> associateBillsToCategory(
            @PathVariable UUID id,
            @RequestBody List<Map<String, UUID>> associateBills
    ) {
        List<UUID> billIds = associateBills.stream()
                .map(map -> map.get("id"))
                .collect(Collectors.toList());

        try {
            categoryService.associateBillsToCategory(id, billIds);
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
}