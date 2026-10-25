package com.example.product.service;

import com.example.product.dto.ProductRequest;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(ProductRequest request) {
        Product product = new Product(
            request.name().trim(),
            request.description(),
            request.price(),
            request.stockQuantity()
        );
        return repository.save(product);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product " + id + " was not found"));
    }
}
