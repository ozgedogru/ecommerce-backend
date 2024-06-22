package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getAllProducts(int offset, int limit);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto saveProduct(Long categoryId, Product product);
    ProductResponseDto updateProduct(Long id, Product product);
    ProductResponseDto deleteProduct(Long id);


    List<ProductResponseDto> getFilteredAndSortedProducts(Long categoryId, String filter, String sort, int offset, int limit);
    Long getTotalProductCount();
    Long getTotalFilteredProductCount(Long categoryId, String filter);


}
