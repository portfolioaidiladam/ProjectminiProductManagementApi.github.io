package com.productapi.service;

import com.productapi.dto.LoginRequest;
import com.productapi.dto.RegisterRequest;
import com.productapi.dto.JwtResponse;
import com.productapi.entity.User;

public interface UserService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    User registerUser(RegisterRequest registerRequest);
}