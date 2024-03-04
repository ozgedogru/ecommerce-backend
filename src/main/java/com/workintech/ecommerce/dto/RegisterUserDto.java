package com.workintech.ecommerce.dto;

public record RegisterUserDto(String fullName,
                              String email,
                              String password,
                              String address,
                              String creditCard) {

}
