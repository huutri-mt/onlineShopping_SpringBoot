package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.mapper.UserMapper;
import com.example.onlineshopping.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(UrlConstant.API_V1_User)
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserMapper userMapper;
    @GetMapping()
    public ApiResponse<List<UserResponse>> getUsers(){
       ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        List<User> users = userServiceImpl.getUsers();
        List<UserResponse> userResponses = users.stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
        apiResponse.setData(userResponses);
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") int id){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userServiceImpl.getUserById(id);
        UserResponse userResponse = userMapper.toUserResponse(user);
        apiResponse.setData(userResponse);
        return apiResponse;
    }
    @PutMapping("/{id}")
    public ApiResponse <String> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable("id") int id){
        ApiResponse <String> apiResponse = new ApiResponse<>();
        userServiceImpl.updateUser(request,id);
        apiResponse.setMessage("Update user thanh cong");
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse <User> deleteUser(@PathVariable("id") int id){
        userServiceImpl.deleteUser(id);
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa người dùng thành công");
        return apiResponse;
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<User> changePassword(@PathVariable ("id") int id, @RequestBody @Valid UserChangePassword request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userServiceImpl.changePassword(id, request);
        apiResponse.setMessage("Cap nhat thanh cong");
        return apiResponse;
    }
}
