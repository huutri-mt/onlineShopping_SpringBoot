package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.IntrospectRequest;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;

import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstant.API_V1_Login)
public class LogginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/log-in")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = loginService.login(request);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(loginResponse);
        apiResponse.setMessage("Đăng nhập thành công");
        return apiResponse;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request){
        IntrospectResponse introspectResponse = loginService.introspect(request);
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(introspectResponse);
        apiResponse.setMessage("Introspect thành công");
        return apiResponse;
    }


}
