package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Response.IpnResponse;

import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params);
}
