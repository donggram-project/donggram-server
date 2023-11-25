package com.donggram.back.repository;

import com.donggram.back.entity.College;
import com.donggram.back.entity.ImageClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageClubRepository extends JpaRepository<ImageClub, Long> {

    Optional<ImageClub> findByClubId(Long clubId);

}
