package com.workintech.ecommerce.dto;

public record AddressOrderResponseDto(
        Long id,
        String addressTitle,
        String nameSurname,
        String city,
        String district
        ) {
}
