package com.example.onlineshopping.mapper;

import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFullname(user.getFullname());
        userResponse.setStatus(user.getStatus());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    public User toUser(UserCreationRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
        user.setRole(request.getRole());
        return user;
    }

    public User toUser(User user, UserUpdateRequest request) {
        if (request == null || user == null) {
            return null;
        }

        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        return user;
    }
}
