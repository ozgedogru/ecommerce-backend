package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       LocalDateTime orderDate,
                       String cardHolderName,
                       String cardNumber,
                       String expirationDate,
                       double price,
                       Long userId,
                       AddressOrderResponseDto address,
                       List<ProductOrderResponseDto> products) {
}
