package com.donggram.back.repository;

import com.donggram.back.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {

    Optional<College> findByName(String collegeName);
}
