package com.donggram.back.repository;

import com.donggram.back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByStudentId(String student_id);
    Optional<Member> findByName(String username);
}
