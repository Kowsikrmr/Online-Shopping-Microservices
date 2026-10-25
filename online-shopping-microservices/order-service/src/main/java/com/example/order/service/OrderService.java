package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.ProductResponse;
import com.example.order.entity.CustomerOrder;
import com.example.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final RestClient productRestClient;

    public OrderService(OrderRepository repository, RestClient productRestClient) {
        this.repository = repository;
        this.productRestClient = productRestClient;
    }

    public CustomerOrder create(CreateOrderRequest request) {
        ProductResponse product;
        try {
            product = productRestClient.get()
                .uri("/products/{id}", request.productId())
                .retrieve()
                .body(ProductResponse.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot create order: product does not exist");
            }
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY, "Product Service returned an error");
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Product Service is unavailable");
        }

        if (product == null || product.id() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY, "Product Service returned an empty response");
        }

        if (request.quantity() > product.stockQuantity()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Requested quantity exceeds available stock");
        }

        CustomerOrder order = new CustomerOrder(
            product.id(), product.name(), request.quantity(), product.price());
        return repository.save(order);
    }

    public CustomerOrder findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order " + id + " was not found"));
    }

    public List<CustomerOrder> findAll() {
        return repository.findAll();
    }
}
