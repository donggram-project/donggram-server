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

    @Query("SELECT c FROM Club c WHERE " +
            "(c.clubName LIKE %:keyword% OR c.content LIKE %:keyword%)")
    List<Club> searchClubs(@Param("keyword") String keyword);

    Optional<Club> findByClubName(String clubName);
}
