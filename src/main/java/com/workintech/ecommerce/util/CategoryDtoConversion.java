package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDtoConversion {

    public static CategoryResponseDto convertCategory(Category category) {
        return new CategoryResponseDto(
                category.getCode(),
                category.getTitle(),
                category.getImage(),
                category.getRating(),
                category.getGender());
    }

    public static List<CategoryResponseDto> convertCategoryList(List<Category> categories) {
        List<CategoryResponseDto> categoryResponses = new ArrayList<>();
        categories.forEach(category ->
                categoryResponses.add(new CategoryResponseDto(
                        category.getCode(),
                        category.getTitle(),
                        category.getImage(),
                        category.getRating(),
                        category.getGender())));
        return categoryResponses;
    }
}
