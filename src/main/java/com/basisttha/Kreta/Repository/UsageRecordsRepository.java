package com.basisttha.Kreta.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.UsageRecords;

public interface UsageRecordsRepository extends JpaRepository<UsageRecords, UUID>{
    
}
