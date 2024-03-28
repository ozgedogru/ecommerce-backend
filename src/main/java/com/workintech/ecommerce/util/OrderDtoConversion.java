package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.OrderDto;
import com.workintech.ecommerce.dto.ProductOrderResponseDto;
import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoConversion {

    public static OrderDto convertToDto(Order order) {
        List<ProductOrderResponseDto> productDtos = order.getProducts().stream()
                .map(product -> new ProductOrderResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage()))
                .collect(Collectors.toList());

        return new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getCardHolderName(),
                order.getCardNumber(),
                order.getExpirationDate(),
                order.getPrice(),
                order.getAddress().getId(),
                order.getUser().getId(),
                productDtos
        );
    }

    public static Order convertToEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.id());
        order.setOrderDate(orderDto.orderDate());
        order.setCardHolderName(orderDto.cardHolderName());
        order.setCardNumber(orderDto.cardNumber());
        order.setExpirationDate(orderDto.expirationDate());
        order.setPrice(orderDto.price());
        return order;
    }

    public static List<OrderDto> convertOrderList(List<Order> orders) {
        return orders.stream()
                .map(OrderDtoConversion::convertToDto)
                .collect(Collectors.toList());
    }
}
