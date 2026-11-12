package com.agrios.user.service;

import com.agrios.user.dto.request.LoginRequest;
import com.agrios.user.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

}