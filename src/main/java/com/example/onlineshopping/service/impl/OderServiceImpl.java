package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;
import com.example.onlineshopping.entity.CartItem;
import com.example.onlineshopping.entity.Order;
import com.example.onlineshopping.entity.OrderItem;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.repository.*;
import com.example.onlineshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OderServiceImpl implements OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void createOrder(OrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(request.getCartId());

        Order order = new Order();
        order.setDescription(request.getDescription());
        order.setStatus("ORDER_INIT");

        long totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Lưu order trước để lấy ID
        order.setUserId(cartRepository.findUserIdById(request.getCartId()));
        order = orderRepository.save(order); // Lưu và lấy lại order với ID đã được gán

        List<CartItem> selectedCartItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            // Kiểm tra productId của cartItem có nằm trong danh sách selectedItems không
            if (request.getSelectedItems().contains(cartItem.getProductId())) {
                long itemPrice = cartItemRepository.getTotalPriceForProduct(cartItem.getCartId(), cartItem.getProductId());
                totalPrice += itemPrice;

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setOriginalPrice(productRepository.getOriginalPriceById(cartItem.getProductId()));
                orderItem.setPrice(itemPrice);

                orderItems.add(orderItem);

                selectedCartItems.add(cartItem);
            }
        }

        order.setTotalAmount(totalPrice);

        // Lưu các orderItems
        orderItemRepository.saveAll(orderItems);

        // Xóa các CartItem đã được chọn
        cartItemRepository.deleteAll(selectedCartItems);
    }

    public List<OrderResponse> viewOrder(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setDescription(order.getDescription());
            orderResponse.setStatus(order.getStatus());
            orderResponse.setPrice(order.getTotalAmount());

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            StringBuilder productNames = new StringBuilder();

            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findById(orderItem.getProductId()).orElse(null);
                if (product != null) {
                    productNames.append(product.getName()).append(", ");
                }
            }

            if (productNames.length() > 0) {
                productNames.setLength(productNames.length() - 2);
            }

            orderResponse.setProductName(productNames.toString());
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }


}
