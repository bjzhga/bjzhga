package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "profit_loss_records")
public class ProfitLossRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 同样是多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "daily_profit_loss", nullable = false)
    private BigDecimal dailyProfitLoss;

    @Column(name = "total_profit_loss", nullable = false)
    private BigDecimal totalProfitLoss;
}