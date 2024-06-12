package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.config.JwtTokenUtil;
import com.workintech.ecommerce.dto.*;
import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.Payment;
import com.workintech.ecommerce.exception.UserException;
import com.workintech.ecommerce.service.*;
import com.workintech.ecommerce.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Validated
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;
    private AddressService addressService;
    private PaymentService paymentService;
    private OrderService orderService;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, AddressService addressService, PaymentService paymentService, OrderService orderService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<ApplicationUser> users = userService.getAllUsers();
        List<UserInfoDto> userInfoDtoList = UserInfoDtoConversion.convertUserList(users);
        return new ResponseEntity<>(userInfoDtoList, HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable("userId") Long id) {
        ApplicationUser user = UserService.getUserById(id);
        UserInfoDto userInfoDto = UserInfoDtoConversion.convertUser(user);
        return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterUserDto registerUserDto) {
       ApplicationUser registeredUser = authenticationService.register(registerUserDto.fullName(),
                registerUserDto.email(),
                registerUserDto.password(),
                registerUserDto.roleId(),
                registerUserDto.store());

        RegisterResponseDto responseDto = RegisterDtoConversion.convertUser(registeredUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginUserDto loginUserDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password())
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            final String token = jwtTokenUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthenticationResponseDto(token));
        } catch (BadCredentialsException e) {
            throw new UserException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            throw new UserException("An error occurred during authentication. Please try again.", HttpStatus.UNAUTHORIZED);
        }
    }



    @GetMapping("/address")
    public ResponseEntity<AddressDto> getAddressById(Authentication authentication, @PathVariable Long addressId) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();


        Optional<Address> address = addressService.getAddressById(addressId);
        return address.map(value -> new ResponseEntity<>(AddressDtoConversion.convertAddress(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddressesByUserId(Authentication authentication) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        List<Address> addresses = addressService.getAllAddressesByUserId(userId);
        List<AddressDto> addressDtos = AddressDtoConversion.convertAddressList(addresses);
        return new ResponseEntity<>(addressDtos, HttpStatus.OK);
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDto> createAddress(Authentication authentication, @RequestBody Address address) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        Address createdAddress = addressService.createAddress(address, userId);
        AddressDto createdAddressDto = AddressDtoConversion.convertAddress(createdAddress);
        return new ResponseEntity<>(createdAddressDto, HttpStatus.CREATED);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(Authentication authentication, @PathVariable Long addressId, @RequestBody Address updatedAddress) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();
        try {
            Address updatedAddressEntity = addressService.updateAddress(addressId, updatedAddress, userId);
            AddressDto updatedAddressDto = AddressDtoConversion.convertAddress(updatedAddressEntity);
            return new ResponseEntity<>(updatedAddressDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddress(Authentication authentication, @PathVariable Long addressId) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.ok("Address deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByUserId(Authentication authentication) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        List<Payment> payments = paymentService.getAllPaymentsByUserId(userId);
        List<PaymentDto> paymentDtos = PaymentDtoConversion.convertPaymentList(payments);
        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentDto> createPayment(Authentication authentication, @RequestBody Payment payment) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        Payment createdPayment = paymentService.createPayment(payment, userId);
        PaymentDto createdPaymentDto = PaymentDtoConversion.convertToDto(createdPayment);
        return new ResponseEntity<>(createdPaymentDto, HttpStatus.CREATED);
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDto> updatePayment(Authentication authentication, @PathVariable Long paymentId, @RequestBody Payment payment) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        Payment updatedPayment = paymentService.updatePayment(paymentId, payment);
        PaymentDto updatedPaymentDto = PaymentDtoConversion.convertToDto(updatedPayment);
        return new ResponseEntity<>(updatedPaymentDto, HttpStatus.OK);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<String> deletePayment(Authentication authentication, @PathVariable("paymentId") Long paymentId) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        try {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok("Payment deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



//TODO: Add exception handler here
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        Long userId = user.getId();

        List<Order> orders = orderService.getOrdersById(userId);

        List<OrderDto> orderDtos = OrderDtoConversion.convertOrderList(orders);

        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }


    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof ApplicationUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }

            ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
            Long userId = user.getId();

            order.setUser(UserService.getUserById(userId));
            Order createdOrder = orderService.createOrder(order, userId);

            OrderDto createdOrderDto = OrderDtoConversion.convertToDto(createdOrder);

            return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);

    }


    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}