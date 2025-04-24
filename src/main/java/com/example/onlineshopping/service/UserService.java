package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.entity.User;

import java.util.List;

public interface UserService {
    User creatUser(UserCreationRequest request);
    List<User> getUsers();
    User getUserById(int id);
    User updateUser(UserUpdateRequest request, int id);
    void deleteUser(int id);
}
