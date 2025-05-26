package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.InitPaymentRequest;
import com.example.onlineshopping.dto.Request.OrderRequest;
import com.example.onlineshopping.dto.Response.OrderResponse;
import com.example.onlineshopping.dto.Response.InitPaymentResponse;
import com.example.onlineshopping.entity.*;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.repository.*;
import com.example.onlineshopping.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.onlineshopping.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.onlineshopping.constan.OrderStatus;
import com.example.onlineshopping.constan.PaymentMethod;


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

    @Autowired
    private VNPayService vnPayService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(request.getCartId());
        if (cartItems == null || cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_FOUND_OR_EMPTY);
        }

        Order order = new Order();
        order.setDescription(request.getDescription());
        order.setStatus(OrderStatus.ORDER_INIT);
        order.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));

        int userId = cartRepository.findUserIdById(request.getCartId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        order.setUser(user);

        order = orderRepository.save(order); // Lưu order và lấy ID

        long totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> selectedCartItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            if (request.getSelectedItems().contains(cartItem.getProduct().getId())) {
                Product product = productRepository.findById(cartItem.getProduct().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NO_EXISTED));

                long itemPrice = product.getPrice();
                long originalPrice = product.getOriginalPrice();
                int quantity = cartItem.getQuantity();

                totalPrice += itemPrice * quantity;

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setOriginalPrice(originalPrice);
                orderItem.setPrice(itemPrice * quantity);

                orderItems.add(orderItem);
                selectedCartItems.add(cartItem);
            }
        }

        order.setTotalAmount(totalPrice);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(selectedCartItems);

        OrderResponse response = new OrderResponse();
        response.setDescription(order.getDescription());
        response.setStatus(order.getStatus().name());
        response.setPrice(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setPayment("UNPAID");

        StringBuilder productNames = new StringBuilder();
        for (OrderItem item : orderItems) {
            productNames.append(item.getProduct().getName()).append(", ");
        }
        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2); // Remove last ", "
        }
        response.setProductName(productNames.toString());

        // Nếu là thanh toán qua VNPay
        if (order.getPaymentMethod() == PaymentMethod.VNPAY) {
            InitPaymentRequest paymentRequest = InitPaymentRequest.builder()
                    .amount(order.getTotalAmount())
                    .txnRef(String.valueOf(order.getId()))
                    .ipAddress(request.getIpAddress())
                    .requestId(UUID.randomUUID().toString())
                    .userId((long) user.getId())

                    .build();

            InitPaymentResponse vnpResponse = vnPayService.init(paymentRequest);
            response.setVnpayUrl(vnpResponse.getVnpUrl());
        }

        return response;
    }


    @PreAuthorize("hasRole('admin') or #id == authentication.token.claims['userId']")
    public List<OrderResponse> getOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setDescription(order.getDescription());
            orderResponse.setStatus(order.getStatus().name());
            orderResponse.setPrice(order.getTotalAmount());

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            StringBuilder productNames = new StringBuilder();

            for (OrderItem orderItem : orderItems) {
                Product product = productRepository
                        .findById(orderItem.getProduct().getId())
                        .orElse(null);
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
        Order order =
                orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDescription(order.getDescription());
        orderResponse.setStatus(order.getStatus().name());
        orderResponse.setPrice(order.getTotalAmount());

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        StringBuilder productNames = new StringBuilder();

        for (OrderItem orderItem : orderItems) {
            Product product =
                    productRepository.findById(orderItem.getProduct().getId()).orElse(null);
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
        Order order =
                orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if ("ORDER_COMPLETED".equals(order.getStatus()) || "DELIVERY_SUCCESS".equals(order.getStatus())) {
            throw new RuntimeException("Không thể hủy đơn hàng");
        }
        order.setStatus(OrderStatus.ORDER_CANCLE);
        orderRepository.save(order);
    }
    @Transactional
    public void markOrder(int orderId, Map<String, String> params) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        String responseCode = params.get("vnp_ResponseCode"); // Mã kết quả trả về từ VNPay
        String transactionStatus = params.get("vnp_TransactionStatus"); // 00: Thành công

        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            order.setStatus(OrderStatus.ORDER_SUCCESS);
            // Có thể thêm logic: lưu payment info
        } else {
            order.setStatus(OrderStatus.ORDER_FAIL); // Thất bại do lỗi hoặc bị hủy
        }

        orderRepository.save(order);
    }


}
