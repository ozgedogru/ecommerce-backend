package com.workintech.ecommerce.dto;

public record OrderProductDto(Long productId,
                              int count,
                              String detail) {
}
