package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoConversion {

    public static ProductResponseDto convertProduct(Product product) {
        return new ProductResponseDto(
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImage(),
                CategoryDtoConversion.convertCategory(product.getCategory())
        );
    }

    public static List<ProductResponseDto> convertProductList(List<Product> products) {
        return products.stream()
                .map(ProductDtoConversion::convertProduct)
                .collect(Collectors.toList());
    }
}
