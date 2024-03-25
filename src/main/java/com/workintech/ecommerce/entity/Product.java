package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product", schema = "ecommerce")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Column(name = "price")
    private double price;

    @Column(name = "stock")
    private int stock;

    @Min(value = 0, message = "Rating value must be at least 0")
    @Max(value = 5, message = "Rating value must be at most 5")
    @Column(name = "rating")
    private double rating;

    @Column(name = "sell_count")
    private int sellCount;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToMany
    @JoinTable(
            name = "order_product", schema = "ecommerce",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;
}
