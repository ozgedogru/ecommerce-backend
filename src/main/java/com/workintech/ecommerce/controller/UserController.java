package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.*;
import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.entity.Payment;
import com.workintech.ecommerce.service.*;
import com.workintech.ecommerce.util.AddressDtoConversion;
import com.workintech.ecommerce.util.OrderDtoConversion;
import com.workintech.ecommerce.util.PaymentDtoConversion;
import com.workintech.ecommerce.util.UserInfoDtoConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, AddressService addressService, PaymentService paymentService, OrderService orderService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<ApplicationUser> users = userService.getAllUsers();
        List<UserInfoDto> userInfoDtoList = UserInfoDtoConversion.convertUserList(users);
        return new ResponseEntity<>(userInfoDtoList, HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable("userId") Long id) {
        ApplicationUser user = userService.getUserById(id);
        UserInfoDto userInfoDto = UserInfoDtoConversion.convertUser(user);
        return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserDto registerUserDto) {
        authenticationService.register(registerUserDto.fullName(),
                registerUserDto.email(),
                registerUserDto.password(),
                registerUserDto.roleId(),
                registerUserDto.store());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId) {
        Optional<Address> address = addressService.getAddressById(addressId);
        return address.map(value -> new ResponseEntity<>(AddressDtoConversion.convertAddress(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddressesByUserId(@PathVariable("userId") Long userId) {
        List<Address> addresses = addressService.getAllAddressesByUserId(userId);
        List<AddressDto> addressDtos = AddressDtoConversion.convertAddressList(addresses);
        return new ResponseEntity<>(addressDtos, HttpStatus.OK);
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<AddressDto> createAddress(@RequestBody Address address, @PathVariable Long userId) {
        Address createdAddress = addressService.createAddress(address, userId);
        AddressDto createdAddressDto = AddressDtoConversion.convertAddress(createdAddress);
        return new ResponseEntity<>(createdAddressDto, HttpStatus.CREATED);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable("addressId") Long id, @RequestBody Address updatedAddress) {
        try {
            addressService.updateAddress(id, updatedAddress);
            return ResponseEntity.ok("Address updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/{userId}/payments")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByUserId(@PathVariable("userId") Long userId) {
        List<Payment> payments = paymentService.getAllPaymentsByUserId(userId);
        List<PaymentDto> paymentDtos = PaymentDtoConversion.convertPaymentList(payments);
        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);
    }

    @PostMapping("/{userId}/payments")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody Payment payment, @PathVariable("userId") Long userId) {
        Payment createdPayment = paymentService.createPayment(payment, userId);
        PaymentDto createdPaymentDto = PaymentDtoConversion.convertToDto(createdPayment);
        return new ResponseEntity<>(createdPaymentDto, HttpStatus.CREATED);
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable("paymentId") Long paymentId, @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(paymentId, payment);
        PaymentDto updatedPaymentDto = PaymentDtoConversion.convertToDto(updatedPayment);
        return new ResponseEntity<>(updatedPaymentDto, HttpStatus.OK);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable("paymentId") Long paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok("Payment deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserId(@PathVariable("userId") Long userId) {
        List<Order> orders = orderService.getOrdersById(userId);
        List<OrderDto> orderDtos = OrderDtoConversion.convertOrderList(orders);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody Order order, @PathVariable("userId") Long userId) {
        order.setUser(UserService.getUserById(userId));
        Order createdOrder = orderService.createOrder(order, userId);
        OrderDto createdOrderDto = OrderDtoConversion.convertToDto(createdOrder);
        return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(orderId, order);
        OrderDto updatedOrderDto = OrderDtoConversion.convertToDto(updatedOrder);
        return new ResponseEntity<>(updatedOrderDto, HttpStatus.OK);
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