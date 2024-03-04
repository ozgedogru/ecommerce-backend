package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.RegisterUserDto;
import com.workintech.ecommerce.dto.UserInfoDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.service.AuthenticationService;
import com.workintech.ecommerce.service.UserService;
import com.workintech.ecommerce.util.UserInfoDtoConversion;
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
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<ApplicationUser> users = userService.getAllUsers();
        List<UserInfoDto> userInfoDtoList = UserInfoDtoConversion.convertUserList(users);
        return new ResponseEntity<>(userInfoDtoList, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable("id") Long id) {
        ApplicationUser user = userService.getUserById(id);
        UserInfoDto userInfoDto = UserInfoDtoConversion.convertUser(user);
        return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
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
