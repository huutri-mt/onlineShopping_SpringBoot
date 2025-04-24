package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        System.out.println(user);
        if(user == null) {
            throw new RuntimeException("Khong tim thay nguoi dung");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mat khau khong dung");
        }
        return true;
    }

}