package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.mapper.UserMapper;
import com.example.onlineshopping.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(UrlConstant.API_V1_User)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping()
    public ApiResponse<List<UserResponse>> getUsers(){
       ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        List<User> users = userService.getUsers();
        List<UserResponse> userResponses = users.stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
        apiResponse.setData(userResponses);
        return apiResponse;
    }

    @GetMapping("/id/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") int id){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.getUserById(id);
        UserResponse userResponse = userMapper.toUserResponse(user);
        apiResponse.setData(userResponse);
        return apiResponse;
    }

    @GetMapping("/email/{email}")
    public ApiResponse<UserResponse> getUserByEmail(@PathVariable("email") String email){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.getByEmail(email);
        UserResponse userResponse = userMapper.toUserResponse(user);
        apiResponse.setData(userResponse);
        return apiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo(){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        UserResponse userResponse = userService.getMyInfo();
        apiResponse.setData(userResponse);
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse <String> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable("id") int id){
        ApiResponse <String> apiResponse = new ApiResponse<>();
        userService.updateUser(request,id);
        apiResponse.setMessage("Update user thanh cong");
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse <User> deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa người dùng thành công");
        return apiResponse;
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<User> changePassword(@PathVariable ("id") int id, @RequestBody @Valid UserChangePassword request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.changePassword(id, request);
        apiResponse.setMessage("Cap nhat thanh cong");
        return apiResponse;
    }
    @PatchMapping("/{id}/block")
    public ApiResponse<User> blockUser(@PathVariable ("id") int id) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.blockUser(id);
        apiResponse.setMessage("Block user thanh cong");
        return apiResponse;
    }
}
