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

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.author = :author ORDER BY a.createdAt DESC")
    Page<Article> findByAuthor(@Param("author") Member author, Pageable pageable);

    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.tag.tagName = :tagName ORDER BY a.createdAt DESC")
    Page<Article> findByTagName(@Param("tagName") String tagName, Pageable pageable);
}
