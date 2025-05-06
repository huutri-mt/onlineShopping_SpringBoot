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
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String description;

    @Column(nullable = false)
    String status;

    @Column(name = "total_amount")
    @JsonProperty("total_amount")
    long totalAmount;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    int userId;
}
