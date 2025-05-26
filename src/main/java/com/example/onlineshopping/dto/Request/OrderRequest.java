package com.example.onlineshopping.dto.Request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    int cartId;
    String description;
    List<Integer> selectedItems;

    String paymentMethod; // VNPAY, COD, etc.
    String ipAddress;     // Dùng khi thanh toán qua VNPay
}
