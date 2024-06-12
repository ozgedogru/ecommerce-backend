package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.RegisterResponseDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import org.springframework.http.ResponseEntity;

public class RegisterDtoConversion {
    public static RegisterResponseDto convertUser(ApplicationUser user) {
        return new RegisterResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}