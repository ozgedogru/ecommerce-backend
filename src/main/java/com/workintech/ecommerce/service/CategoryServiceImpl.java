package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.exception.CategoryException;
import com.workintech.ecommerce.repository.CategoryRepository;
import com.workintech.ecommerce.util.CategoryDtoConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryDtoConversion.convertCategoryList(categories);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return CategoryDtoConversion.convertCategory(category);
        }
        throw new CategoryException("Category not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public Category getCategoryByIdOriginal(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        throw new CategoryException("Category with given id is not exist: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public CategoryResponseDto saveCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return CategoryDtoConversion.convertCategory(savedCategory);
    }

    @Override
    public CategoryResponseDto deleteCategory(Long id) {

        CategoryResponseDto categoryResponseDto = getCategoryById(id);
        categoryRepository.deleteById(id);
        return categoryResponseDto;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryException("Category not found.", HttpStatus.NOT_FOUND));
        return category.getProducts();
    }
}
