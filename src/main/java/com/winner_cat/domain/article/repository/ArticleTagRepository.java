package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag,Long> {
}
