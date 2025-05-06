package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.AddCartItemRequest;
import com.example.onlineshopping.dto.Request.RemoveCartItemRequest;
import com.example.onlineshopping.dto.Response.CartResponse;
import com.example.onlineshopping.entity.CartItem;

import java.util.List;


public interface CartService {
    CartItem addToCart(AddCartItemRequest request);
    void removeFromCart(RemoveCartItemRequest request);
    void removeAllFromCart(int cartId);
    List<CartResponse> getCart(int cartId);
}
