package com.example.onlineshopping.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    int price;
    int stock;
    @JsonProperty("original_price")
    int originalPrice;
    String category;

    @OneToMany (mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItem;
    @OneToMany (mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItem;
}
