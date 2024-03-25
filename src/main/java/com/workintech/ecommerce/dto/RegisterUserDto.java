package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.Store;

import java.util.List;
import java.util.Set;

public record RegisterUserDto(String fullName,
                              String email,
                              String password,
                              Long roleId,
                              Store store) {
}
