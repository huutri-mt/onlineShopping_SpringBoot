package com.example.onlineshopping.controller;

import com.example.onlineshopping.constan.UrlConstant;
import com.example.onlineshopping.dto.Request.ProductRequest;
import com.example.onlineshopping.dto.Response.ApiResponse;
import com.example.onlineshopping.dto.Response.ProductResponse;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.mapper.ProductMapper;
import com.example.onlineshopping.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstant.API_V1_Products)
public class ProductController {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ApiResponse<String> createProduct(@RequestBody ProductRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productService.createProduct(request);
        apiResponse.setMessage("Them san pham thanh cong");
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<String> updateProduct(@PathVariable("id") int id, @RequestBody ProductRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productService.updateProduct(id, request);
        apiResponse.setMessage("Update san pham thanh cong");
        return apiResponse;
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getProducts() {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("/id/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable("id") int id) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        Product product = productService.getProductById(id);
        ProductResponse productResponse = productMapper.toProductResponse(product);
        apiResponse.setData(productResponse);
        return apiResponse;
    }

    @GetMapping("name/{name}")
    public ApiResponse<List<ProductResponse>> getProductsByName(@PathVariable("name") String name) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productService.getProductsByName(name);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("category/{category}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable("category") String category) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public ApiResponse<List<ProductResponse>> getProductsByPriceRange(
            @PathVariable("minPrice") Double minPrice, @PathVariable("maxPrice") Double maxPrice) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
        apiResponse.setData(productResponses);
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Product> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa sản phẩm thành công");
        return apiResponse;
    }
}
