package com.winner_cat.domain.scrap.repository;

import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Page<Scrap> findByMember(Member member, Pageable pageable);
}
