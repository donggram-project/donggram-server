package com.donggram.back.repository;

import com.donggram.back.entity.Club;
import com.donggram.back.entity.ClubJoin;
import com.donggram.back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubJoinRepository extends JpaRepository<ClubJoin, Long> {
    Optional<ClubJoin> findByMemberIdAndClubId(Long memberId, Long clubId);
    Optional<ClubJoin> findByMemberAndClub(Member member, Club club);

}
