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

public class orderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @JsonProperty("order_id")
    int orderID;
    @JsonProperty("product_id")
    int productID;
    @JsonProperty("original_price")
    int originalPrice;
    @JsonProperty("price")
    int price;
}
