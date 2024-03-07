package com.workintech.ecommerce.dto;

import com.workintech.ecommerce.entity.Address;

import java.util.List;

public record RegisterUserDto(String fullName,
                              String email,
                              String password) {

}
