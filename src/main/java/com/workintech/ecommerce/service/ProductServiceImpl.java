package com.workintech.ecommerce.service;

import com.workintech.ecommerce.dto.CategoryResponseDto;
import com.workintech.ecommerce.dto.ProductResponseDto;
import com.workintech.ecommerce.entity.Category;
import com.workintech.ecommerce.entity.Product;
import com.workintech.ecommerce.exception.ProductException;
import com.workintech.ecommerce.repository.ProductRepository;
import com.workintech.ecommerce.util.ProductDtoConversion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final EntityManager entityManager;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.entityManager = entityManager;
    }

    @Override
    public List<ProductResponseDto> getAllProducts(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> products = productPage.getContent();
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

    @Override
    public List<ProductResponseDto> getFilteredAndSortedProducts(Long categoryId, String filter, String sort, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        Predicate predicate = cb.conjunction();

        if (categoryId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
        }

        if (filter != null && !filter.isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + filter.toLowerCase() + "%"));
        }

        cq.where(predicate);

        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(":");
            String sortBy = sortParams[0];
            String sortOrder = sortParams[1];

            if (sortOrder.equalsIgnoreCase("asc")) {
                cq.orderBy(cb.asc(root.get(sortBy)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(root.get(sortBy)));
            }
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(limit);

        List<Product> products = typedQuery.getResultList();
        return ProductDtoConversion.convertProductList(products);
    }

    @Override
    public Long getTotalProductCount() {
        return productRepository.count();
    }

    @Override
    public Long getTotalFilteredProductCount(Long categoryId, String filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> root = cq.from(Product.class);

        Predicate predicate = cb.conjunction();

        if (categoryId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
        }

        if (filter != null && !filter.isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + filter.toLowerCase() + "%"));
        }

        cq.select(cb.count(root)).where(predicate);

        return entityManager.createQuery(cq).getSingleResult();
    }
}