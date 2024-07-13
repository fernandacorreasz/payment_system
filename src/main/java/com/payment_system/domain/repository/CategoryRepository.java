package com.payment_system.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.payment_system.domain.model.Bill.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Page<Category> findByNameCategoryContaining(String nameCategory, Pageable pageable);
    boolean existsByNameCategory(String nameCategory);
}