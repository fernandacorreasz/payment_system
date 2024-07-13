package com.payment_system.domain.model.user;

import java.util.Set;

public record RegisterDTO(String name, String email, String password, Set<String> roles) {
}
