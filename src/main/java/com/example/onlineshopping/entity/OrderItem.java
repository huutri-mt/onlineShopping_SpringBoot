package com.example.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "order_id", nullable = false)
    @JsonProperty("order_id")
    int orderId;

    @Column(name = "product_id", nullable = false)
    @JsonProperty("product_id")
    int productId;

    @Column(name = "original_price", nullable = false)
    @JsonProperty("original_price")
    long originalPrice;

    @Column(name = "price", nullable = false)
    @JsonProperty("price")
    long price;
}
