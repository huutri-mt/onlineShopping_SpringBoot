package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.AddCartItemRequest;
import com.example.onlineshopping.dto.Request.RemoveCartItemRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstant.API_V1_Cart)
public class CartController {
    @Autowired
    private CartServiceImpl cartServiceImpl;

    @PostMapping("/add")
    public ApiResponse<String> addToCart(@RequestBody AddCartItemRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        cartServiceImpl.addToCart(request);
        apiResponse.setMessage("Thêm sản phẩm vào giỏ hàng thành công");
        return apiResponse;
    }
    @DeleteMapping("/remove")
    public ApiResponse<String> removeFromCart(@RequestBody RemoveCartItemRequest request ){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        cartServiceImpl.removeFromCart(request);
        apiResponse.setMessage("Xóa sản phẩm khỏi giỏ hàng thành công");
        return apiResponse;
    }
    @DeleteMapping("/removeAll/{id}")
    public ApiResponse<String> removeAllFromCart(@PathVariable("id") int id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        cartServiceImpl.removeAllFromCart(id);
        apiResponse.setMessage("Xóa tất cả sản phẩm khỏi giỏ hàng thành công");
        return apiResponse;
    }
}
