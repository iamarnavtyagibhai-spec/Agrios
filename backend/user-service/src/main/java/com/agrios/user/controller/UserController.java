package com.agrios.user.controller;
import com.agrios.user.exception.ResourceAlreadyExistsException;
import com.agrios.user.dto.request.SignupRequest;
import com.agrios.user.dto.response.UserResponse;
import com.agrios.user.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request) {

        UserResponse response = userService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}