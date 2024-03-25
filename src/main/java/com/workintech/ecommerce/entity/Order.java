package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order", schema = "ecommerce")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "price")
    private double price;

    @Setter
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "order_product", schema = "ecommerce",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    public Address getAddress() {
        if (this.address == null) {
            this.address = new Address();
        }
        return this.address;
    }

    public List<Product> getProducts() {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        return this.products;
    }


}
