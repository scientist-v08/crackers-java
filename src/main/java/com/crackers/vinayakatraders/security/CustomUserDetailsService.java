package com.crackers.vinayakatraders.security;

import com.crackers.vinayakatraders.dto.LoginUserDetailsProjection;
import com.crackers.vinayakatraders.entity.Role;
import com.crackers.vinayakatraders.repository.RoleRepository;
import com.crackers.vinayakatraders.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        LoginUserDetailsProjection login =
                userRepository
                        .findLoginDetailsByEmail(email)
                        .stream()
                        .findFirst()
                        .orElseThrow(() ->
                                new UsernameNotFoundException(email));

        return new CustomUserDetails(
                login.email(),
                login.roleName()
        );
    }

}
