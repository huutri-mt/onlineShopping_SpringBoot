package com.example.onlineshopping.service.impl;

import com.example.onlineshopping.dto.Request.ProductRequest;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.mapper.ProductMapper;
import com.example.onlineshopping.repository.ProductRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;
    @PreAuthorize("hasRole('admin')")
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay san pham"));
    }
    @PreAuthorize("hasRole('admin')")
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
       product = productMapper.toProduct(request);

        return productRepository.save(product);
    }
    @PreAuthorize("hasRole('admin')")
    public Product updateProduct(int id, ProductRequest request) {
        Product product = getProductById(id);
        if(productRepository.existsByName(request.getName()) && !product.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        product = productMapper.toProduct(product,request);

        return productRepository.save(product);
    }
    @PreAuthorize("hasRole('admin')")
    public void deleteProduct(int id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
