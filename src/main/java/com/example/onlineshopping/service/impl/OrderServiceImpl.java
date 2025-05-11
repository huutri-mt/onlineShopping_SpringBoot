package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;
import com.example.onlineshopping.entity.*;
import com.example.onlineshopping.repository.*;
import com.example.onlineshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

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
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createOrder(OrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(request.getCartId());
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không tồn tại hoặc trống");
        }

        Order order = new Order();
        order.setDescription(request.getDescription());
        order.setStatus("ORDER_INIT");

        int userId = cartRepository.findUserIdById(request.getCartId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        order.setUser(user);

        order = orderRepository.save(order); // Lưu order và lấy ID

        long totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> selectedCartItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            if (request.getSelectedItems().contains(cartItem.getProduct().getId())) {
                Product product = productRepository.findById(cartItem.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                long itemPrice = product.getPrice();
                long originalPrice = product.getOriginalPrice();

                totalPrice += itemPrice * cartItem.getQuantity(); // Nhân số lượng

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setOriginalPrice(originalPrice);
                orderItem.setPrice(itemPrice * cartItem.getQuantity());

                orderItems.add(orderItem);
                selectedCartItems.add(cartItem);
            }
        }

        order.setTotalAmount(totalPrice);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(selectedCartItems);
    }

    public List<OrderResponse> getOrdersByUserId(int userId) {
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
                Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
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

    public OrderResponse getOrderByOrderId(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDescription(order.getDescription());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setPrice(order.getTotalAmount());

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        StringBuilder productNames = new StringBuilder();

        for (OrderItem orderItem : orderItems) {
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
            if (product != null) {
                productNames.append(product.getName()).append(", ");
            }
        }

        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        }

        orderResponse.setProductName(productNames.toString());
        return orderResponse;
    }

    public void cancleOrder(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        
        if ("ORDER_COMPLETED".equals(order.getStatus()) ||"DELIVERY_SUCCESS".equals(order.getStatus())  ) {
            throw new RuntimeException("Không thể hủy đơn hàng");
        }
        order.setStatus("ORDER_CANCLE");
        orderRepository.save(order);
    }

}
