package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;

import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstant.API_V1_Login)
public class LogginController {
    @Autowired
    private LoginServiceImpl loginServiceImpl;
    @PostMapping
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = loginServiceImpl.login(request);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(loginResponse);
        apiResponse.setMessage("Đăng nhập thành công");
        return apiResponse;
    }

}
