package com.example.onlineshopping.mapper;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFullname(user.getFullname());
        userResponse.setStatus(user.getStatus());
        userResponse.setRole(user.getRole());
        return userResponse;
    }
}
