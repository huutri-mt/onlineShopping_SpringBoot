package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Response.OrderResponse;
import com.example.onlineshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstant.API_V1_Order)
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/create")
    public ApiResponse<String> createOrder(@RequestBody OrderRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.createOrder(request);
        apiResponse.setMessage("Tao don hang thanh cong");
        return apiResponse;
    }

    @GetMapping("/view/{userId}")
    public ApiResponse<List<OrderResponse>> viewOrder(@PathVariable int userId){
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        List<OrderResponse> orderResponses = orderService.viewOrder(userId);
        apiResponse.setData(orderResponses);
        apiResponse.setMessage("Xem don hang thanh cong");
        return apiResponse;
    }
}
