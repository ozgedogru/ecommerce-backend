package com.workintech.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "category", schema = "ecommerce")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "img")
    private String image;

    @Min(value = 0, message = "Rating value must be at least 0")
    @Max(value = 5, message = "Rating value must be at most 5")
    @Column(name = "rating")
    private int rating;

    @Column(name = "gender")
    private char gender;

    @OneToMany(mappedBy = "category")
    private List<Product> products;


    //Function: Kategoriye urun ekler (null pointer ex. almamak icin)
    public void addProduct(Product product) {
        if(products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
    }
}
