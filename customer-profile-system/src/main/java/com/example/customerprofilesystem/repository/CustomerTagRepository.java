package com.example.customerprofilesystem.repository;

import com.example.customerprofilesystem.model.Customer;
import com.example.customerprofilesystem.model.CustomerTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerTagRepository extends JpaRepository<CustomerTag, Long> {
    @Transactional
    void deleteByCustomer(Customer customer);
}