package com.example.onlineshopping.repository.httpclient;

import com.example.onlineshopping.dto.Response.ExchangeTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "facebook-identity", url = "https://graph.facebook.com")
public interface FacebookIdentityClient {

    @PostMapping(value = "/v18.0/oauth/access_token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@RequestBody Map<String, ?> formData);
}
