package com.example.onlineshopping.util;

import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${valid-duration}")
    private long VALID_DURATION;

    @Value("${referesh-valid-duration}")
    private long REFRESH_VALID_DURATION;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("HT")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .claim("authorities", List.of("ROLE_" + user.getRole()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return signedJWT.verify(jwsVerifier)
                && expirationTime.after(new Date())
                && !invalidatedTokenRepository.existsById(
                        signedJWT.getJWTClaimsSet().getJWTID());
    }

    public SignedJWT verifyToken(String token, Boolean isRefresh) throws JOSEException {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expirationTime = (isRefresh)
                    ? new Date(signedJWT
                            .getJWTClaimsSet()
                            .getExpirationTime()
                            .toInstant()
                            .plus(REFRESH_VALID_DURATION, ChronoUnit.SECONDS)
                            .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            boolean isSignatureValid = signedJWT.verify(jwsVerifier);
            boolean isNotExpired = expirationTime.after(new Date());
            boolean isNotRevoked = !invalidatedTokenRepository.existsById(jti);

            if (!(isSignatureValid && isNotExpired && isNotRevoked)) {
                throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
    }
}
