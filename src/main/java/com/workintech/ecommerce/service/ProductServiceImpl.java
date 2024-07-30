package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.exception.ProductException;
import com.workintech.ecommerce.repository.ProductRepository;
import com.workintech.ecommerce.util.ProductDtoConversion;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<ProductResponseDto> getProducts(Long categoryId, String filter, String sort, int limit, int offset) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortField = "id";

        if (sort != null && !sort.isEmpty()) {
            sortDirection = (sort.toLowerCase().contains("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
            if (sort.contains(":")) {
                sortField = sort.split(":")[0];
            }
        }

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(sortDirection, sortField));

        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (categoryId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (filter != null && !filter.isEmpty()) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filter.toLowerCase() + "%");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(namePredicate, descriptionPredicate));
            }

            return predicate;
        };

        Page<Product> productsPage = productRepository.findAll(spec, pageable);

        return productsPage.map(ProductDtoConversion::convertProduct);
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
