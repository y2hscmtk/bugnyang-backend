package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.author = :author ORDER BY a.createdAt DESC")
    Page<Article> findByAuthor(@Param("author") Member author, Pageable pageable);

    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.tag.tagName = :tagName ORDER BY a.createdAt DESC")
    Page<Article> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    // 오늘 작성된 게시글 찾기
    @Query("SELECT a FROM Article a WHERE a.createdAt >= :startTime AND a.createdAt <= :endTime")
    List<Article> findAllByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Article a JOIN a.tags at JOIN at.tag t JOIN a.scrapList s WHERE s.member.id = :memberId AND t.tagName = :tagName ORDER BY a.createdAt DESC")
    Page<Article> findScrappedArticlesByTag(@Param("memberId") Long memberId, @Param("tagName") String tagName, Pageable pageable);
}
