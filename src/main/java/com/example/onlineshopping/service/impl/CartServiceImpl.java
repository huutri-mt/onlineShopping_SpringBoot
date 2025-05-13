package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.AddCartItemRequest;
import com.example.onlineshopping.dto.Request.RemoveCartItemRequest;
import com.example.onlineshopping.dto.Response.CartResponse;
import com.example.onlineshopping.entity.Cart;
import com.example.onlineshopping.entity.CartItem;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.CartItemRepository;
import com.example.onlineshopping.repository.CartRepository;
import com.example.onlineshopping.repository.ProductRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

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
        // Kiểm tra số lượng
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Lấy userId từ JWT
        Long userIdLong = ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("userId");

        int userId = userIdLong.intValue();


        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NO_EXISTED));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        return cartItemRepository.save(cartItem);
    }


    public void removeFromCart(RemoveCartItemRequest request) {
        // Lấy userId từ JWT
        Long userIdLong = ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("userId");

        int userId = userIdLong.intValue();

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        // Kiểm tra cartItem có thuộc về user đang đăng nhập không
        if (cartItem.getCart() == null || cartItem.getCart().getUser() == null ||
                cartItem.getCart().getUser().getId() != userId) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa giỏ hàng này");
        }

        int quantity = cartItem.getQuantity() - request.getQuantity();
        if (quantity > 0) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
    }

    @Transactional
    public void removeAllFromCart(int cartId){
        Long userIdLong = ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("userId");

        int userId = userIdLong.intValue();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Gio hang khong ton tai"));

        if (cart.getUser() == null || cart.getUser().getId() != userId) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa giỏ hàng này");
        }

        cartItemRepository.deleteByCart(cart);
    }

    public List<CartResponse> getCart(int cartId) {
        Long userIdLong = ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("userId");

        int userId = userIdLong.intValue();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Gio hang khong ton tai"));

        if (cart.getUser() == null || cart.getUser().getId() != userId) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa giỏ hàng này");
        }

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

