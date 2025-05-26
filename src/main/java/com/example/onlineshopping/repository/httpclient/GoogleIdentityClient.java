package com.example.onlineshopping.repository.httpclient;

import com.example.onlineshopping.dto.Response.ExchangeTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;



@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface GoogleIdentityClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@RequestBody Map<String, ?> formData);

}
