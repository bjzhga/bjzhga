package com.example.customerprofilesystem.repository;

import com.example.customerprofilesystem.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 使用JPQL进行连接查询，一次性抓取所需数据，避免N+1问题
    @Query("SELECT c FROM Customer c " +
            "LEFT JOIN FETCH c.riskProfile " +
            "LEFT JOIN FETCH c.holdings " +
            "LEFT JOIN FETCH c.transactions " +
            "LEFT JOIN FETCH c.customerTags " +
            "WHERE c.id = :id")
    Optional<Customer> findByIdWithDetails(Long id);
}