package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getProducts(Long categoryId, String filter, String sort);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto saveProduct(Long categoryId, Product product);
    ProductResponseDto updateProduct(Long id, Product product);
    ProductResponseDto deleteProduct(Long id);
}
