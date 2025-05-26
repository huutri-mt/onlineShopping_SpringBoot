package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.PasswordCreationRequest;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.mapper.UserMapper;
import com.example.onlineshopping.service.UserService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(UrlConstant.API_V1_User)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ApiResponse<?> getUsers(
            @RequestParam(required = false) Integer id, @RequestParam(required = false) String email) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();

        if (id != null) {
            User user = userService.getUserById(id);
            apiResponse.setData(userMapper.toUserResponse(user));
            return apiResponse;
        }

        if (email != null) {
            User user = userService.getByEmail(email);
            apiResponse.setData(userMapper.toUserResponse(user));
            return apiResponse;
        }

        List<User> users = userService.getUsers();
        List<UserResponse> userResponses =
                users.stream().map(userMapper::toUserResponse).toList();
        apiResponse.setData(userResponses);
        return apiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        UserResponse userResponse = userService.getMyInfo();
        apiResponse.setData(userResponse);
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<String> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable("id") int id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.updateUser(request, id);
        apiResponse.setMessage("Update user thanh cong");
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<User> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa người dùng thành công");
        return apiResponse;
    }

    @PostMapping("/create-password")
    public ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        userService.createPassword(request);
        apiResponse.setMessage("Tao  mat khau thanh cong");
        return apiResponse;
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<User> changePassword(
            @PathVariable("id") int id, @RequestBody @Valid UserChangePassword request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.changePassword(id, request);
        apiResponse.setMessage("Cap nhat thanh cong");
        return apiResponse;
    }

    @PatchMapping("/{id}/block")
    public ApiResponse<User> blockUser(@PathVariable("id") int id) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.blockUser(id);
        apiResponse.setMessage("Block user thanh cong");
        return apiResponse;
    }
}
