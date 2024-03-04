package com.workintech.ecommerce.controller;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.service.CategoryService;
import com.workintech.ecommerce.util.CategoryDtoConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
@CrossOrigin("*")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> saveCategory(@RequestBody Category category) {
        CategoryResponseDto savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable("id") Long id) {
        CategoryResponseDto deletedCategory = categoryService.deleteCategory(id);
        return ResponseEntity.ok(deletedCategory);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<Product> products = categoryService.getProductsByCategoryId(categoryId);
        List<ProductResponseDto> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(new ProductResponseDto(
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    new CategoryResponseDto(
                            product.getCategory().getCode(),
                            product.getCategory().getTitle(),
                            product.getCategory().getImage(),
                            product.getCategory().getRating(),
                            product.getCategory().getGender()
                    )
            ));
        }
        return ResponseEntity.ok(productResponses);
    }


}