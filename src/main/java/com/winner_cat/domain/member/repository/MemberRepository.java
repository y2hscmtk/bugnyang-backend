package com.winner_cat.domain.member.repository;

import com.winner_cat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsMemberByEmail(String email);
    Optional<Member> findMemberByEmail(String email);
    Member findByUsername(String username);
}
