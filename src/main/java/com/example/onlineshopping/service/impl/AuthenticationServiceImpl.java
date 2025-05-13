package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.IntrospectRequest;
import com.example.onlineshopping.dto.Request.LoginRequest;
import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.dto.Request.LogoutRequest;
import com.example.onlineshopping.entity.InvalidatedToken;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.InvalidatedTokenRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.AuthenticationService;
import com.example.onlineshopping.util.JwtUtil;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.JOSEException;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new AppException(ErrorCode.USER_NO_EXISTED);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        if ("block".equalsIgnoreCase(user.getStatus())) {
            throw new AppException(ErrorCode.ACC_BLOCK);
        }

        String token = jwtUtil.generateToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();
        if (token == null || token.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        IntrospectResponse introspectResponse = new IntrospectResponse();

        try {
            introspectResponse.setValid(jwtUtil.validateToken(token));
        } catch (ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
        catch (JOSEException e) {
            throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        return introspectResponse;
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            var signToken = jwtUtil.verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiryTime);

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
    }



}
