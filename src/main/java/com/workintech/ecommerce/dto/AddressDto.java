package com.workintech.ecommerce.dto;

public record AddressDto(Long id,
                         String addressTitle,
                         String nameSurname,
                         String phone,
                         String city,
                         String district,
                         String neighborhood,
                         String addressDir) {
}

