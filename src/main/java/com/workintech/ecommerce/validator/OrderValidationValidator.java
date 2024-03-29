package com.workintech.ecommerce.validator;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.Order;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderValidationValidator implements ConstraintValidator<OrderValidation, Order> {

    @Override
    public boolean isValid(Order order, ConstraintValidatorContext context) {
        if (order == null) {
            return false;
        }
        return isValidOrder(order);
    }

    private boolean isValidOrder(Order order) {
        return hasProducts(order) && hasValidAddress(order.getAddress());
    }

    private boolean hasProducts(Order order) {
        return order.getProducts() != null && !order.getProducts().isEmpty();
    }

    private boolean hasValidAddress(Address address) {
        return address != null;
    }
}

