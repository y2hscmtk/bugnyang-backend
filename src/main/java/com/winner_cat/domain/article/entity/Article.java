package com.winner_cat.domain.article.entity;

import com.winner_cat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Article")
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title; // 게시글 제목
    private String cause; // 원인
    private String solution; // 해결 방법

    @OneToMany(mappedBy = "articleId")
    private List<ArticleTag> tags = new ArrayList<>();

    public void changeTags(ArrayList<ArticleTag> articleTag) {
        this.tags = articleTag;
    }
}
