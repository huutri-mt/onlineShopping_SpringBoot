package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;

import com.example.onlineshopping.service.impl.LoginService;
import com.example.onlineshopping.service.impl.UserServiceImpl;
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
    @PostMapping
    public ApiResponse<?> login(@RequestBody LoginRequest request){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        loginService.login(request);
        apiResponse.setMessage("Dang nhap thanh cong");
        return apiResponse;
    }

}
