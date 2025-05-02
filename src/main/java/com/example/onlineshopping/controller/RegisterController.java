package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstant.API_V1_Register)
public class RegisterController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @PostMapping
    public ApiResponse<User> createUser (@RequestBody @Valid UserCreationRequest request){
        ApiResponse <User> apiResponse = new ApiResponse<>();
        apiResponse.setData(userServiceImpl.creatUser(request));
        apiResponse.setMessage("Tao tai khoan thanh cong");
        return apiResponse;
    }
}
