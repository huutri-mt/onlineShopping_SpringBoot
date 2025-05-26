package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.InitPaymentRequest;
import com.example.onlineshopping.dto.Response.InitPaymentResponse;

public interface PaymentService {
    InitPaymentResponse init(InitPaymentRequest request);
}
