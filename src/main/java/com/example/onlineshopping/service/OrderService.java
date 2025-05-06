package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;

import java.util.List;

public interface OrderService {
    void createOrder(OrderRequest request);
    //void cancelOrder(int orderId);
    //void completeOrder(int orderId);
    List<OrderResponse> viewOrder(int userId);
}
