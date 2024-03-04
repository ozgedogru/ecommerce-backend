package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.RegisterUserDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUserDto registerUserDto) {
        return authenticationService.register(registerUserDto.fullName(), registerUserDto.email(), registerUserDto.password());

    }
}
