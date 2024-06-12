package com.workintech.ecommerce.dto;

public class AuthenticationResponseDto {

    private final String token;

    public AuthenticationResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
