package com.basisttha.Kreta.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.Plan;

public interface PlanRepository extends JpaRepository<Plan, UUID>{
    
}
