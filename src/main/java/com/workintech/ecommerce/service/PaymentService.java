package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.entity.Payment;
import com.workintech.ecommerce.repository.PaymentRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getAllPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment, Long userId) {
        ApplicationUser user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        payment.setUser(user);
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        payment.setCardNumber(updatedPayment.getCardNumber());
        payment.setExpirationDate(updatedPayment.getExpirationDate());
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
