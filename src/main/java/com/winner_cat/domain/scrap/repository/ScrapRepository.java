package com.winner_cat.domain.scrap.repository;

import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByMember(Member member);
}
