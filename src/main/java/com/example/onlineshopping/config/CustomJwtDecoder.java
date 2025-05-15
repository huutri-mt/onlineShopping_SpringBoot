package com.example.onlineshopping.config;

import com.example.onlineshopping.dto.Request.IntrospectRequest;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import com.example.onlineshopping.exception.AppException;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) {
        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build()
        );

        if (!response.isValid()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            return NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build()
                    .decode(token);
        } catch (JwtException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN); 
        }

    }

}
