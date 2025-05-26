package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.*;
import com.example.onlineshopping.dto.Response.ExchangeTokenResponse;
import com.example.onlineshopping.dto.Response.IntrospectResponse;
import com.example.onlineshopping.dto.Response.LoginResponse;
import com.example.onlineshopping.dto.Response.OutboundUserResponse;
import com.example.onlineshopping.entity.InvalidatedToken;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.InvalidatedTokenRepository;
import com.example.onlineshopping.repository.httpclient.FacebookIdentityClient;
import com.example.onlineshopping.repository.httpclient.FacebookUserClient;
import com.example.onlineshopping.repository.httpclient.GoogleIdentityClient;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.repository.httpclient.GoogleUserClient;
import com.example.onlineshopping.service.AuthenticationService;
import com.example.onlineshopping.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Date;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
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
    private FacebookIdentityClient facebookIdentityClient;

    @Autowired
    private GoogleUserClient googleUserClient;

    @Autowired
    private FacebookUserClient facebookUserClient;

    @Autowired
    private GoogleIdentityClient googleIdentityClient;

    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${facebook.client-id}")
    private String facebookClientId;
    @Value("${facebook.client-secret}")
    private String facebookClientSecret;
    @Value("${facebook.redirect-uri}")
    private String facebookRedirectUri;

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

    public LoginResponse outboundAuthenticate(String code, String provider) {
        Map<String, String> formData = new HashMap<>();
        String clientId;
        String clientSecret;
        String redirectUri;
        if ("google".equalsIgnoreCase(provider)) {
            clientId = googleClientId;
            clientSecret = googleClientSecret;
            redirectUri = googleRedirectUri;
        } else if ("facebook".equalsIgnoreCase(provider)) {
            clientId = facebookClientId;
            clientSecret = facebookClientSecret;
            redirectUri = facebookRedirectUri;
        } else {
            throw new AppException(ErrorCode.UNKNOWN_PROVIDER);
        }

        formData.put("code", code);
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("redirect_uri", redirectUri);
        formData.put("grant_type", "authorization_code");
        log.info("Sending formData: {}", formData);

        ExchangeTokenResponse response;
        OutboundUserResponse userInfo;

        try {
            if ("google".equalsIgnoreCase(provider)) {
                response = googleIdentityClient.exchangeToken(formData);
                log.info("Token response: {}", response);
                userInfo = googleUserClient.getUserInfo("json", response.getAccessToken());
            } else {
                response = facebookIdentityClient.exchangeToken(formData);
                log.info("Token response: {}", response);
                userInfo = facebookUserClient.getUserInfo("id,name,email,picture", response.getAccessToken());

            }
        }  catch (FeignException e) {
        String errorMessage = e.contentUTF8();
        log.error("FeignException status: {}, content: {}", e.status(), errorMessage, e);

        if (errorMessage.contains("redirect_uri_mismatch")) {
            throw new AppException(ErrorCode.REDIRECT_URI_MISMATCH);
        }

        throw new AppException(ErrorCode.OAUTH_PROVIDER_ERROR);
    }

    // Kiểm tra hoặc tạo user
        User user = userRepository.findByEmail(userInfo.getEmail());
        if (user == null) {
            user = new User();
            user.setFullname(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            user.setStatus("active");
            user.setRole("user");
            userRepository.save(user);
        }
        log.info("user: {}",user);

        String jwt = jwtUtil.generateToken(user);
        log.info("token: {}",jwt);
        return LoginResponse.builder().token(jwt).build();
    }

}
