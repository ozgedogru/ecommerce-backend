package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Address;

import java.util.List;

public record UserInfoDto(Long id, String fullName, String email,
                          List<AddressDto> addresses) {
}

