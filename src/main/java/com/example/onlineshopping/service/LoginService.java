package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}
