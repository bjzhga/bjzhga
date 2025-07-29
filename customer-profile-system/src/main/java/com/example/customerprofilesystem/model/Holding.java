package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "holdings", uniqueConstraints = {
        // 对应数据库中的 UNIQUE KEY `uk_customer_fund` (`customer_id`, `fund_code`)
        @UniqueConstraint(columnNames = {"customer_id", "fund_code"})
})
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 多对一关系：多个持仓记录可以属于同一个客户
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "fund_code", nullable = false)
    private String fundCode;

    @Column(name = "fund_name")
    private String fundName;

    @Column(name = "total_shares", nullable = false)
    private BigDecimal totalShares;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;
}