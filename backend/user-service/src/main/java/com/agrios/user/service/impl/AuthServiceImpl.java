package com.agrios.user.service.impl;

import com.agrios.user.dto.request.LoginRequest;
import com.agrios.user.dto.response.AuthResponse;
import com.agrios.user.entity.User;
import com.agrios.user.repository.UserRepository;
import com.agrios.user.service.AuthService;
import com.agrios.user.service.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {

        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password"));

        // 2. Check whether account is enabled
        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }

        // 3. Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new BadCredentialsException("Invalid email or password");
        }

        // 4. Generate JWT token
        String token = jwtService.generateToken(user.getEmail());

        // 5. Return authentication response
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }
}