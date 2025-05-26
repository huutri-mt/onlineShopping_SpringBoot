package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    void cancleOrder(int orderId);

    List<OrderResponse> getOrdersByUserId(int userId);

    OrderResponse getOrderByOrderId(int orderId);

    void markOrder(int orderId, Map<String, String> params);
}
