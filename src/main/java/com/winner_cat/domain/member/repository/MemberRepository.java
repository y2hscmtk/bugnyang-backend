package com.winner_cat.domain.member.repository;

import com.winner_cat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsMemberByUsername(String username);
}
