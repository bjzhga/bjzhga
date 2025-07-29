package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "fund_code", nullable = false)
    private String fundCode;

    @Column(name = "fund_name")
    private String fundName;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "transaction_shares", nullable = false)
    private BigDecimal transactionShares;

    @Column(name = "transaction_price")
    private BigDecimal transactionPrice;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
}