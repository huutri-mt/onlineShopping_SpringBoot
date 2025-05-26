package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Response.OrderResponse;
import com.example.onlineshopping.service.OrderService;
import java.util.List;

import com.example.onlineshopping.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstant.API_V1_Order)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request,
                                                  HttpServletRequest httpServletRequest) {
        String ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        request.setIpAddress(ipAddress);
        OrderResponse orderResponse = orderService.createOrder(request);
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Tạo đơn hàng thành công");
        apiResponse.setData(orderResponse);

        return apiResponse;
    }


    @GetMapping("/viewByUser/{userId}")
    public ApiResponse<List<OrderResponse>> viewOrdersByUser(@PathVariable int userId) {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        List<OrderResponse> orderResponses = orderService.getOrdersByUserId(userId);
        apiResponse.setData(orderResponses);
        apiResponse.setMessage("Xem don hang thanh cong");
        return apiResponse;
    }

    @GetMapping("/viewByOrder/{orderId}")
    public ApiResponse<OrderResponse> viewOrderByOrderId(@PathVariable int orderId) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        OrderResponse orderResponses = orderService.getOrderByOrderId(orderId);
        apiResponse.setData(orderResponses);
        apiResponse.setMessage("Xem don hang thanh cong");
        return apiResponse;
    }

    @DeleteMapping("/cancel/{orderId}")
    public ApiResponse<String> cancelOrder(@PathVariable int orderId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.cancleOrder(orderId);
        apiResponse.setMessage("Huy don hang thanh cong");
        return apiResponse;
    }
}
