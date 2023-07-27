package com.donggram.back.repository;

import com.donggram.back.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findClubsByCollegeId(Long collegeId);
}
