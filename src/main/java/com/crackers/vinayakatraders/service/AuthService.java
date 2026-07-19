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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        List<LoginUserDetailsProjection> details = userRepository.findLoginDetailsByEmail(request.email());

        if(details.isEmpty()) {
            throw new UsernameNotFoundException("Email ID doesn't exist. Sign up before login.");
        }

        LoginUserDetailsProjection first = details.getFirst();

        if (!passwordEncoder.matches(request.password(), first.password())) {
            throw new BadCredentialsException("Invalid credentials. Incorrect password.");
        }

        List<Routes> routes = details.stream()
                .map(r -> new Routes(r.routeId(), r.route(), r.heading(), r.roleId()))
                .toList();

        String token = jwtUtil.generateToken(first.email(), first.roleName());

        boolean isAdmin = first.roleName().equals("ROLE_ADMIN");

        return new LoginResponse(token, routes, isAdmin);
    }
}
