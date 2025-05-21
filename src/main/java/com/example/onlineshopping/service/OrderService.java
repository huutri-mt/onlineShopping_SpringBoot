package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;
import java.util.List;

public interface OrderService {
    void createOrder(OrderRequest request);

    void cancleOrder(int orderId);

    List<OrderResponse> getOrdersByUserId(int userId);

    OrderResponse getOrderByOrderId(int orderId);
}
