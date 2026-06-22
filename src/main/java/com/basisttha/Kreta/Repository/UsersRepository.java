package com.basisttha.Kreta.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.Users;

public interface UsersRepository extends JpaRepository<Users, UUID>{
    Optional<Users> findByEmail(String email);
}
