package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;

import java.util.List;

public interface CategoryService{
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);
    Category getCategoryByIdOriginal(Long id);
    CategoryResponseDto saveCategory(Category category);
    CategoryResponseDto deleteCategory(Long id);
    List<Product> getProductsByCategoryId(Long categoryId);
}
