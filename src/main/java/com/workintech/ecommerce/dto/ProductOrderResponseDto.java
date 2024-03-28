package com.workintech.ecommerce.dto;

public record ProductOrderResponseDto(
        Long id,
        String name,
        double price,
        String image) {
}
