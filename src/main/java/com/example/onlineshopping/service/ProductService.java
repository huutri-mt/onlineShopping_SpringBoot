package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.Request.ProductRequest;
import com.example.onlineshopping.entity.Product;
import java.util.List;

public interface ProductService {
    Product getProductById(int id);

    Product createProduct(ProductRequest request);

    Product updateProduct(int id, ProductRequest request);

    void deleteProduct(int id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);
}
