package com.workintech.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       LocalDateTime orderDate,
                       String cardHolderName,
                       String cardNumber,
                       String expirationDate,
                       double price,
                       Long addressId,
                       Long userId,
                       List<Long> productIds) {
}
