package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthor(Member author);
}
