package com.workintech.ecommerce.dto;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        double price,
        int stock,
        double rating,
        String image,
        CategoryResponseDto category) {
}
