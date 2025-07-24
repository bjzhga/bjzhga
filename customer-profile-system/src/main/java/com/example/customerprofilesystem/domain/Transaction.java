package com.example.customerprofilesystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    private String fundCode;
    private String fundName;
    private String transactionType;
    private BigDecimal transactionAmount;
    private BigDecimal transactionShares;
    private BigDecimal transactionPrice;
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    @JsonIgnore
    private Customer customer;
}