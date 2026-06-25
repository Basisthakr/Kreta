package com.basisttha.Kreta.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.Plan;

public interface PlanRepository extends JpaRepository<Plan, UUID>{
    Optional<Plan> findByPlanName(String name);
}
