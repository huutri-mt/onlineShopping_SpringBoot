package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.IntrospectRequest;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Request.RefreshRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Request.LogoutRequest;
import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(UrlConstant.API_V1_Auth)
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/log-in")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = authenticationService.login(request);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(loginResponse);
        apiResponse.setMessage("Đăng nhập thành công");
        return apiResponse;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request){
        IntrospectResponse introspectResponse = authenticationService.introspect(request);
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(introspectResponse);
        apiResponse.setMessage("Introspect thành công");
        return apiResponse;
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Đăng xuất thành công");
        return apiResponse;
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody RefreshRequest request) {
        LoginResponse loginResponse = authenticationService.refreshToken(request);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Refresh thành công");
        apiResponse.setData(loginResponse);
        return apiResponse;
    }





}
