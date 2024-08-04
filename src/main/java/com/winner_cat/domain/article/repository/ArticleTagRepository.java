package com.winner_cat.domain.article.repository;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.member.entity.Member;
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
    @Query("SELECT at FROM ArticleTag at JOIN at.article a WHERE at.tag = :tag ORDER BY a.createdAt DESC")
    Page<ArticleTag> findArticleTagPageByTag(Tag tag, Pageable pageable);

    // 내가 작성한 게시글 중에서 태그 정보로 조회
    @Query("SELECT at FROM ArticleTag at JOIN at.article a WHERE a.author = :member and at.tag = :tag ORDER BY a.createdAt DESC")
    Page<ArticleTag> findMyArticleTagPageByTag(Member member, Tag tag, Pageable pageable);

    // 태그 이름으로 묶어서 내림차순 추출
    @Query("SELECT at.tag.tagName, COUNT(at) FROM ArticleTag at WHERE at.article IN :articles GROUP BY at.tag.tagName ORDER BY COUNT(at) DESC")
    List<Object[]> findTopTagsByArticles(@Param("articles") List<Article> articles);

    // 회원이 스크랩한 게시글 중 특정 태그에 해당하는 게시글 페이징 조회
    @Query("SELECT at FROM ArticleTag at JOIN at.article a JOIN a.scrapList sc WHERE sc.member = :member and at.tag = :tag ORDER BY a.createdAt DESC")
    Page<ArticleTag> findScrapArticleTagPageByTag(Member member, Tag tag, Pageable pageable);
}
