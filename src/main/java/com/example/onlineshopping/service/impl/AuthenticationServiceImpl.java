package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.*;
import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.entity.InvalidatedToken;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.InvalidatedTokenRepository;
import com.example.onlineshopping.repository.httpclient.OutboundIdentityClient;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.repository.httpclient.OutboundUserClient;
import com.example.onlineshopping.service.AuthenticationService;
import com.example.onlineshopping.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;


@Slf4j
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

    @Autowired
    private OutboundUserClient outboundUserClient;

    @Autowired
    private  OutboundIdentityClient outboundIdentityClient;
    @NonFinal
    @Value("${client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${client-secret}")
    protected String CLIENT_SECRET ;

    @NonFinal
    @Value("${redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected String GRANT_TYPE = "authorization_code";

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
            throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
        }
        return introspectResponse;
    }

    public void logout(LogoutRequest request) {
        try {
            var signToken = jwtUtil.verifyToken(request.getToken(), true);

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

    public LoginResponse refreshToken(RefreshRequest request) {
        try {
            var signToken = jwtUtil.verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiryTime);

            invalidatedTokenRepository.save(invalidatedToken);

            String email = signToken.getJWTClaimsSet().getSubject();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NO_EXISTED);
            }
            String token = jwtUtil.generateToken(user);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            return loginResponse;

        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
    }

    public LoginResponse outboundAuthenticate(String code) {
            Map<String, String> formData = new HashMap<>();
            formData.put("code", code);
            formData.put("client_id", CLIENT_ID);
            formData.put("client_secret", CLIENT_SECRET);
            formData.put("redirect_uri", REDIRECT_URI);
            formData.put("grant_type", GRANT_TYPE);

            var response = outboundIdentityClient.exchangeToken(formData);
            var userInfor = outboundUserClient.getUserInfo("json", response.getAccessToken());

            User user = userRepository.findByEmail(userInfor.getEmail());
            if (user == null){
                user = new User();
                user.setFullname(userInfor.getName());
                user.setEmail(userInfor.getEmail());
                user.setStatus("active");
                user.setRole("user");

                userRepository.save(user);
            }

            var token = jwtUtil.generateToken(user);
            return LoginResponse.builder()
                    .token(token)
                    .build();
        }


}
