package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.Store;

import java.util.List;

public record UserInfoDto(Long id,
                          String fullName,
                          String email,
                          List<AddressDto> addresses,
                          List<PaymentDto> payments,
                          List<OrderDto> orders,
                          Role role,
                          StoreDto store) {
}

