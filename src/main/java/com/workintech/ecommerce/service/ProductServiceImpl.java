package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.exception.ProductException;
import com.workintech.ecommerce.repository.ProductRepository;
import com.workintech.ecommerce.util.ProductDtoConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Autowired


    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductDtoConversion.convertProductList(products);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ProductDtoConversion.convertProduct(product);
        }
        throw new ProductException("Product not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public ProductResponseDto saveProduct(Long categoryId, Product product) {

        Category category = categoryService.getCategoryByIdOriginal(categoryId);

        category.addProduct(product);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDto(product.getName(), product.getPrice(), product.getStock(), new CategoryResponseDto(category.getId(), category.getCode(),
                category.getTitle(), category.getImage(), category.getRating(), category.getGender()));
    }


    @Override
    public ProductResponseDto updateProduct(Long id, Product product) {
       ProductResponseDto productResponseDto = getProductById(id);
        Product updatedProduct = productRepository.save(product);
        return productResponseDto;
    }

    @Override
    public ProductResponseDto deleteProduct(Long id) {
       ProductResponseDto productResponseDto = getProductById(id);
       productRepository.deleteById(id);
       return productResponseDto;
    }
}
