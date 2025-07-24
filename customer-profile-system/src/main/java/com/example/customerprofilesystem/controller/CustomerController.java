package com.example.customerprofilesystem.controller;

import com.example.customerprofilesystem.domain.Customer;
import com.example.customerprofilesystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    // 这个是我们核心的画像查询接口
    @GetMapping("/{id}/profile")
    public ResponseEntity<Customer> getCustomerProfile(@PathVariable Long id) {
        // 使用我们自定义的JOIN FETCH查询，提高性能
        Optional<Customer> customerOptional = customerRepository.findByIdWithDetails(id);
        return customerOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}