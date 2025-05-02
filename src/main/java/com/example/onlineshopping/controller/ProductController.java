package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.ProductRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Response.ProductResponse;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.mapper.ProductMapper;
import com.example.onlineshopping.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstant.API_V1_Products)
public class ProductController {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductServiceImpl productServiceImpl;
    @PostMapping("/create")
    public ApiResponse<String> createProduct(@RequestBody ProductRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productServiceImpl.createProduct(request);
        apiResponse.setMessage("Them san pham thanh cong");
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<String> updateProduct(@PathVariable("id") int id, @RequestBody ProductRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productServiceImpl.updateProduct(id, request);
        apiResponse.setMessage("Update san pham thanh cong");
        return apiResponse;
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getProducts() {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productServiceImpl.getAllProducts();
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("/id/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable("id") int id) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        Product product = productServiceImpl.getProductById(id);
        ProductResponse productResponse = productMapper.toProductResponse(product);
        apiResponse.setData(productResponse);
        return apiResponse;
    }

    @GetMapping("name/{name}")
    public ApiResponse<List<ProductResponse>> getProductsByName(@PathVariable("name") String name) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productServiceImpl.getProductsByName(name);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("category/{category}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable("category") String category) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productServiceImpl.getProductsByCategory(category);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public ApiResponse<List<ProductResponse>> getProductsByPriceRange(@PathVariable("minPrice") Double minPrice, @PathVariable("maxPrice") Double maxPrice) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productServiceImpl.getProductsByPriceRange(minPrice, maxPrice);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Product> deleteProduct (@PathVariable("id") int id){
        productServiceImpl.deleteProduct(id);
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa sản phẩm thành công");
        return apiResponse;
    }

}
