package com.FinacialRDS.Financialrisk.repository;

import com.FinacialRDS.Financialrisk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring gives you save(), findById(), and delete() automatically!
}