package com.example.order.controller;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.entity.CustomerOrder;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrder create(@Valid @RequestBody CreateOrderRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<CustomerOrder> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CustomerOrder getById(@PathVariable Long id) {
        return service.findById(id);
    }
}
