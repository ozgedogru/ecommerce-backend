package com.workintech.ecommerce.dto;

import java.time.LocalDateTime;

public record RegisterResponseDto(
        Long id,
        String fullName,
        String email) {
}
