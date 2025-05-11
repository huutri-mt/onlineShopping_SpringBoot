package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.AddCartItemRequest;
import com.example.onlineshopping.dto.Request.RemoveCartItemRequest;
import com.example.onlineshopping.dto.Response.CartResponse;
import com.example.onlineshopping.entity.Cart;
import com.example.onlineshopping.entity.CartItem;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.repository.CartItemRepository;
import com.example.onlineshopping.repository.CartRepository;
import com.example.onlineshopping.repository.ProductRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public CartItem addToCart(AddCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId());

        if (cart == null) {
            cart = new Cart();
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(RemoveCartItemRequest request){
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        int quantity = cartItem.getQuantity() - request.getQuantity();
        if (quantity > 0){
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        else {
            cartItemRepository.delete(cartItem);
        }
    }
    public void removeAllFromCart(int cartId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        cartItemRepository.deleteByCartItem(cart.getId());
    }


    public List<CartResponse> getCart(int cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không tồn tại");
        }

        List<CartResponse> cartResponses = new ArrayList<>();
        for (CartItem item : cartItems) {
            CartResponse cartResponse = new CartResponse();
            cartResponse.setProductName(item.getProduct().getName());
            cartResponse.setProductPrice(item.getProduct().getPrice());
            cartResponse.setQuantity(item.getQuantity());
            cartResponse.setTotalPrice(item.getProduct().getPrice() * item.getQuantity());
            cartResponses.add(cartResponse);
        }
        return cartResponses;
    }



}

