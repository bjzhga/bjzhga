package com.example.customerprofilesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "risk_profiles")
public class RiskProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    // JSON字段在JPA中可以映射为String
    @Column(name = "questionnaire_answers", columnDefinition = "JSON")
    private String questionnaireAnswers;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;
}
