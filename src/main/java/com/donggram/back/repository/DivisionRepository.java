package com.donggram.back.repository;

import com.donggram.back.entity.College;
import com.donggram.back.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {
    Optional<Division> findByName(String collegeName);

}
