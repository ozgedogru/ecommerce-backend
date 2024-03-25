package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_payment", schema = "ecommerce")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
}
