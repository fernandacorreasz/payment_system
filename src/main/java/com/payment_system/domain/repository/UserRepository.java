package com.payment_system.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payment_system.domain.model.user.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
    Optional<Users> findByName(String name);
    
}
