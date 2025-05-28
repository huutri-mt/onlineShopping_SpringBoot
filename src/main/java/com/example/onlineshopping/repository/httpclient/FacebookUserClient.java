package com.example.onlineshopping.repository.httpclient;

import com.example.onlineshopping.dto.Response.OutboundUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebook-user-client", url = "https://graph.facebook.com")
public interface FacebookUserClient {

    @GetMapping("/me")
    OutboundUserResponse getUserInfo(@RequestParam("fields") String fields,
                                     @RequestParam("access_token") String accessToken);
}
