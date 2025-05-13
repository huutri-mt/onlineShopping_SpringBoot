package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.IntrospectRequest;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.dto.Request.LogoutRequest;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    IntrospectResponse introspect(IntrospectRequest introspectRequest);
    void logout(LogoutRequest logoutRequest) throws JOSEException;
}
