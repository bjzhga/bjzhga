package com.example.customerprofilesystem.service;

import com.example.customerprofilesystem.model.Customer;
import com.example.customerprofilesystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true) // 使用事务，并设置为只读以优化性能
    public Optional<Customer> findByIdWithDetails(Long id) {
        // 使用JOIN FETCH一次性加载关联数据，避免N+1问题
        Optional<Customer> customerOpt = customerRepository.findById(id);
        customerOpt.ifPresent(customer -> {
            // 显式加载懒加载的集合
            customer.getTags().size();
            customer.getTransactions().size();
            customer.getRiskProfile(); // 访问以触发加载
        });
        return customerOpt;
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
