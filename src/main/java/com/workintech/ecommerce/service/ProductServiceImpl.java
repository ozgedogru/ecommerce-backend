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

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductResponseDto> getProducts(Long categoryId, String filter, String sort) {
        List<Product> products;

        if (categoryId != null) {
            if (filter != null && !filter.isEmpty()) {
                products = productRepository.findByCategoryIdAndFilter(categoryId, filter);
            } else {
                products = productRepository.findByCategoryId(categoryId);
            }
        } else {
            if (filter != null && !filter.isEmpty()) {
                products = productRepository.findByFilter(filter);
            } else {
                products = productRepository.findAll();
            }
        }

        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(":");
            if (sortParams.length == 2) {
                String sortField = sortParams[0];
                String sortDirection = sortParams[1];
                products.sort("asc".equalsIgnoreCase(sortDirection) ?
                        (p1, p2) -> compare(p1, p2, sortField) :
                        (p1, p2) -> compare(p2, p1, sortField));
            }
        }

        return ProductDtoConversion.convertProductList(products);
    }

    private int compare(Product p1, Product p2, String field) {
        switch (field) {
            case "price":
                return Double.compare(p1.getPrice(), p2.getPrice());
            case "rating":
                return Double.compare(p1.getRating(), p2.getRating());
            default:
                return p1.getName().compareToIgnoreCase(p2.getName());
        }
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

        return new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getRating(), product.getImage(), new CategoryResponseDto(category.getId(), category.getCode(),
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
