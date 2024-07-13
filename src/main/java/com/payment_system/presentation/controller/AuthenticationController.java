
package com.payment_system.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.payment_system.domain.model.user.Users;
import com.payment_system.domain.model.user.AuthenticationDTO;
import com.payment_system.domain.model.user.LoginResponseDTO;
import com.payment_system.domain.model.user.RegisterDTO;
import com.payment_system.domain.model.user.Role;
import com.payment_system.domain.repository.UserRepository;
import com.payment_system.domain.repository.RoleRepository;
import com.payment_system.infrastructure.security.TokenService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        Users existingUserByEmail = userRepository.findByEmail(registerDTO.email());
        Optional<Users> existingUserByName = userRepository.findByName(registerDTO.name());
        
        if (existingUserByEmail != null) {
            return ResponseEntity.badRequest().body("Error: Email address already in use");
        }

        if (existingUserByName.isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username already in use.");
        }
        Users user = new Users();
        user.setName(registerDTO.name());
        user.setEmail(registerDTO.email());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : registerDTO.roles()) {
            Role role = roleRepository.findByNameRole(roleName);
            if (role == null) {
                role = new Role();
                role.setNameRole(roleName);
                roleRepository.save(role);
            }
            roles.add(role);
        }
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok("Success: User registered successfully.");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationDTO authenticationDTO) {
        Users user = userRepository.findByEmail(authenticationDTO.email());
        if (user == null || !passwordEncoder.matches(authenticationDTO.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Error: Incorrect email or password.");
        }
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
