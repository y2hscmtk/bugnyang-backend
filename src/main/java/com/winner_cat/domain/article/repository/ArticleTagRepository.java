package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag,Long> {
    void deleteByArticle(Article article);
    List<ArticleTag> findByArticle(Article article);
    List<ArticleTag> findByTag(Tag tag);
    Page<ArticleTag> findArticleTagPageByTag(Tag tag, Pageable pageable);

    // 태그 이름으로 묶어서 내림차순 추출
    @Query("SELECT at.tag.tagName, COUNT(at) FROM ArticleTag at WHERE at.article IN :articles GROUP BY at.tag.tagName ORDER BY COUNT(at) DESC")
    List<Object[]> findTopTagsByArticles(@Param("articles") List<Article> articles);
}
