package com.workintech.ecommerce.dto;

public record ProductResponseDto(
        String name,
        double price,
        int stock,
        String image,
        CategoryResponseDto category) {
}
