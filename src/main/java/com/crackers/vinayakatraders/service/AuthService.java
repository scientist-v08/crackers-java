package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.*;
import com.crackers.vinayakatraders.entity.Role;
import com.crackers.vinayakatraders.entity.Routes;
import com.crackers.vinayakatraders.entity.User;
import com.crackers.vinayakatraders.repository.RoleRepository;
import com.crackers.vinayakatraders.repository.RoutesRepository;
import com.crackers.vinayakatraders.repository.UserRepository;
import com.crackers.vinayakatraders.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoutesRepository routesRepository;

    public JwtResponse registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .roleId(userRole.getId())
                .enabled(true)
                .build();

        User saved = userRepository.save(user);

        String roleName = userRole.getName();
        String token = jwtUtil.generateToken(saved.getEmail(), roleName);

        return new JwtResponse(token, "Bearer", saved.getEmail(), roleName);
    }

    public JwtResponse registerAdmin(AdminSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        User admin = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .roleId(adminRole.getId())
                .enabled(true)
                .build();

        User saved = userRepository.save(admin);

        String roleName = adminRole.getName();
        String token = jwtUtil.generateToken(saved.getEmail(), roleName);

        return new JwtResponse(token, "Bearer", saved.getEmail(), roleName);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.Email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.Password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getEnabled()) {
            throw new RuntimeException("Account disabled");
        }

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        String token = jwtUtil.generateToken(user.getEmail(), role.getName());

        List<Routes> routes = routesRepository.findByRole(user.getRoleId()).stream().toList();

        if (routes.isEmpty()) {
            throw new RuntimeException("Failed to fetch the routes");
        }

        return new LoginResponse(token, routes);
    }
}
