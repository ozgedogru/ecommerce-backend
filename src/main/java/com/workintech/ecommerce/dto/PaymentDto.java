package com.workintech.ecommerce.dto;

public record PaymentDto(Long id,
                         String cardHolder,
                         String cardNumber,
                         String expirationDate,
                         Long userId) {
}

