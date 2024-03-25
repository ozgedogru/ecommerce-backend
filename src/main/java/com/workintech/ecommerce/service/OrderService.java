package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Order;
import com.workintech.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersById(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Order order, Long userId) {
        order.setUser(UserService.getUserById(userId));
        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, Order order) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setCardHolderName(order.getCardHolderName());
        existingOrder.setCardNumber(order.getCardNumber());
        existingOrder.setExpirationDate(order.getExpirationDate());
        existingOrder.setPrice(order.getPrice());
        existingOrder.setAddress(order.getAddress());
        existingOrder.setProducts(order.getProducts());
        existingOrder.setUser(order.getUser());

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
