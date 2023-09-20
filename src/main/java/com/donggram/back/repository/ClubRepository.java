package com.donggram.back.repository;

import com.donggram.back.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
//    List<Club> findClubsByCollegeId(Long collegeId);

    @Query("SELECT c FROM Club c WHERE " +
            "(:keyword IS NULL OR LOWER(c.clubName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:collegeIds IS NULL OR c.college.id IN :collegeIds) " +
            "AND (:divisionIds IS NULL OR c.division.id IN :divisionIds)")
    Page<Club> findClubsByFilters(
            @Param("keyword") String keyword,
            @Param("collegeIds") List<Long> collegeIds,
            @Param("divisionIds") List<Long> divisionIds,
            Pageable pageable);

    Optional<Club> findByClubName(String clubName);
}
