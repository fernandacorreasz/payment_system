package com.payment_system.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payment_system.domain.model.user.Role;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByNameRole(String nameRole);
}
