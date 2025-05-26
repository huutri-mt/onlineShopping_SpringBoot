package com.example.onlineshopping.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String description;
    String productName;
    long price;
    String status;
    String paymentMethod; // "VNPAY", "MOMO", "COD", etc.
    String payment;        // trạng thái thanh toán: "PAID", "UNPAID", etc.

    // Các link thanh toán theo phương thức
    String vnpayUrl;
    String momoUrl;
    String zaloPayUrl;
}
