package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductResponseDto> getProducts(Long categoryId, String filter, String sort, int limit, int offset);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto saveProduct(Long categoryId, Product product);
    ProductResponseDto updateProduct(Long id, Product product);
    ProductResponseDto deleteProduct(Long id);
}
