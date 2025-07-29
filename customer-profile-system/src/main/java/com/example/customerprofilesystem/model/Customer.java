package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_no", unique = true, nullable = false)
    private String customerNo;

    @Column(nullable = false)
    private String name;

    @Column(name = "id_card", unique = true)
    private String idCard;

    private String phone;
    private String education;
    private String profession;

    @Column(name = "annual_income")
    private BigDecimal annualIncome;

    @Column(name = "asset_scale")
    private BigDecimal assetScale;

    @Column(name = "investment_experience")
    private Integer investmentExperience;

    @Column(name = "investment_frequency")
    private String investmentFrequency;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 建立一对多关系：一个客户可以有多条交易记录
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    // 建立一对多关系：一个客户可以有多条标签
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomerTag> tags;

    // 建立一对一关系：一个客户对应一个风险评级
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RiskProfile riskProfile;

    // ================== 新增内容开始 ==================

    // 建立一对多关系：一个客户可以有多条持仓记录
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Holding> holdings;

    // 建立一对多关系：一个客户可以有多条盈亏记录
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProfitLossRecord> profitLossRecords;

    // ================== 新增内容结束 ==================


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}