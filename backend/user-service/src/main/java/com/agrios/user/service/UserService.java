package com.agrios.user.service;

import com.agrios.user.dto.request.SignupRequest;
import com.agrios.user.dto.response.UserResponse;

public interface UserService {

    UserResponse signup(SignupRequest request);

}