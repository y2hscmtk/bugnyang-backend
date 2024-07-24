package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag,Long> {
    void deleteByArticle(Article article);
    List<ArticleTag> findByArticle(Article article);
    List<ArticleTag> findByTag(Tag tag);
    Page<ArticleTag> findArticleTagPageByTag(Tag tag, Pageable pageable);
}
