package com.winner_cat.domain.scrap.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @Query("SELECT sc FROM Scrap sc JOIN sc.article ac WHERE sc.member = :member ORDER BY ac.createdAt DESC")
    Page<Scrap> findByMember(Member member, Pageable pageable);
    // 회원이 게시글을 스크랩했는지 유무
    boolean existsByMemberAndArticle(Member member, Article article);
    Optional<Scrap> findScrapInfoByMemberAndArticle(Member member, Article article);
    void deleteByArticle(Article article);
}
