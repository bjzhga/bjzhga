package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "tag_name"})
})
@NoArgsConstructor // 添加无参构造函数
public class CustomerTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column(name = "tag_source")
    private String tagSource;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 添加一个方便的构造函数
    public CustomerTag(Customer customer, String tagName, String tagSource) {
        this.customer = customer;
        this.tagName = tagName;
        this.tagSource = tagSource;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}