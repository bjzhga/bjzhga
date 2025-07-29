package com.example.customerprofilesystem.repository;

import com.example.customerprofilesystem.model.ProfitLossRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitLossRecordRepository extends JpaRepository<ProfitLossRecord, Long> {
}