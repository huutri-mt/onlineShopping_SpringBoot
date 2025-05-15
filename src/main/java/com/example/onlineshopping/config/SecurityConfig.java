package com.example.onlineshopping.config;

import com.example.onlineshopping.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Lazy;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URLS_POST = { "/api/v1/register", "/api/v1/auth/log-in", "/api/v1/auth/introspect", "/api/v1/auth/logout", "/api/v1/auth/refresh" };
    private static final String[] PUBLIC_URLS_GET = { "/api/v1/products", "/api/v1/products/id/{id}", "/api/v1/products/category/{category}", "/api/v1/products/name/{name}" };
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    @Lazy
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình exception handling trực tiếp qua exceptionHandlingConfigurer
        httpSecurity
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(HttpMethod.POST, PUBLIC_URLS_POST).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_URLS_GET).permitAll()
                                .anyRequest().authenticated()
                );

        // Cấu hình OAuth2 Resource Server và JWT Decoder
        httpSecurity
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)  // Cấu hình JWT Decoder tùy chỉnh
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        // Cấu hình exception handling qua customAuthenticationEntryPoint
        httpSecurity
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint));  // Đưa vào handler lỗi JWT tùy chỉnh

        // Tắt CSRF nếu không cần thiết
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
