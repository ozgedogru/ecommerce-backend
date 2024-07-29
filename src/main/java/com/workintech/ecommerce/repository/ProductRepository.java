package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND (p.name LIKE %:filter% OR p.description LIKE %:filter%)")
    List<Product> findByCategoryIdAndFilter(
            @Param("categoryId") Long categoryId,
            @Param("filter") String filter
    );

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:filter% OR p.description LIKE %:filter%")
    List<Product> findByFilter(@Param("filter") String filter);
}
