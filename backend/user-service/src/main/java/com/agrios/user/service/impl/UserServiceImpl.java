package com.agrios.user.service.impl;

import com.agrios.user.dto.request.SignupRequest;
import com.agrios.user.dto.response.UserResponse;
import com.agrios.user.entity.Role;
import com.agrios.user.entity.User;
import com.agrios.user.exception.ResourceAlreadyExistsException;
import com.agrios.user.repository.UserRepository;
import com.agrios.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest request) {

        // 1. Check whether email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // 2. Check whether phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ResourceAlreadyExistsException("Phone number already registered");
        }

        // 3. Create User object
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fatherName(request.getFatherName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())

                // Location details
                .stateCode(request.getStateCode())
                .districtCode(request.getDistrictCode())
                .tehsilCode(request.getTehsilCode())
                .blockCode(request.getBlockCode())
                .gramPanchayatCode(request.getGramPanchayatCode())
                .villageCode(request.getVillageCode())

                // Assign role from backend
                .role(Role.FARMER)
                .enabled(true)
                .build();

        // 4. Save user in database
        User savedUser = userRepository.save(user);

        // 5. Convert User entity into UserResponse
        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .fatherName(savedUser.getFatherName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .stateCode(savedUser.getStateCode())
                .districtCode(savedUser.getDistrictCode())
                .tehsilCode(savedUser.getTehsilCode())
                .blockCode(savedUser.getBlockCode())
                .gramPanchayatCode(savedUser.getGramPanchayatCode())
                .villageCode(savedUser.getVillageCode())
                .role(savedUser.getRole().name())
                .enabled(savedUser.isEnabled())
                .build();
    }
}