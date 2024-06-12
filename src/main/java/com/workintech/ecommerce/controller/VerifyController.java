package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.config.JwtTokenUtil;
import com.workintech.ecommerce.dto.UserInfoDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.service.UserService;
import com.workintech.ecommerce.util.UserInfoDtoConversion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@Validated
@CrossOrigin("*")
public class VerifyController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public VerifyController(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String username = jwtTokenUtil.extractUsername(token);
            boolean isValidToken = jwtTokenUtil.validateToken(token, username);

            if (isValidToken) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UserInfoDto userInfoDto = UserInfoDtoConversion.convertUser((ApplicationUser) userDetails);
                return ResponseEntity.ok(userInfoDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }
}

