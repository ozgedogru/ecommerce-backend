package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.RegisterUserDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.service.AuthenticationService;
import com.workintech.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationUser>> getAllUsers() {
        List<ApplicationUser> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getUserById(@PathVariable("id") Long id) {
        ApplicationUser user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUserDto registerUserDto) {
        return authenticationService.register(registerUserDto.fullName(), registerUserDto.email(), registerUserDto.password(), registerUserDto.address(), registerUserDto.creditCard());
    }
}
