package com.example.onlineshopping.mapper;

import com.example.onlineshopping.dto.Request.ProductRequest;
import com.example.onlineshopping.dto.Response.ProductResponse;
import com.example.onlineshopping.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toProductResponse(Product product){
        if (product == null) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory());
        return productResponse;
    }

    public Product toProduct(Product product, ProductRequest request){
        if (request == null) {
            return null;
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCategory(request.getCategory());
        return product;
    }

    public Product toProduct (ProductRequest request){
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCategory(request.getCategory());
        return product;
    }
}
