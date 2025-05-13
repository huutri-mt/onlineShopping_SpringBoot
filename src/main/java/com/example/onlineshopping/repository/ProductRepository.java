package com.example.onlineshopping.repository;

import com.example.onlineshopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Boolean existsByName(String name);
    List<Product> findByCategory(String category);
    List<Product> findByName(String name);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    @Query("SELECT p.originalPrice FROM Product p WHERE p.id = :id")
    long getOriginalPriceById(int id);
}
