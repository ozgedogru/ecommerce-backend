package com.workintech.ecommerce.dto;

public record ProductResponseDto(
        Long id,
        String name,
        double price,
        int stock,
        String image,
        CategoryResponseDto category) {
}
