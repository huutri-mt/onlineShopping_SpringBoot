package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.PasswordCreationRequest;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {
    User creatUser(UserCreationRequest request);

    List<User> getUsers();

    User getUserById(int id);

    User updateUser(UserUpdateRequest request, int id);

    void deleteUser(int id);

    Boolean changePassword(int id, UserChangePassword userChangePassword);


    void blockUser(int userId);

    User getByEmail(String email);

    UserResponse getMyInfo();

    void createPassword(PasswordCreationRequest request);
}
