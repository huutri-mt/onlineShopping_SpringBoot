package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.LoginService;
import com.example.onlineshopping.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
        if ("block".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("Tài khoản bị khóa");
        }

        String token = jwtUtil.generateToken(loginRequest.getEmail());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }
}
