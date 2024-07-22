package com.winner_cat.domain.article.entity;

import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.global.entity.BaseEntity;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author; // 게시글 작성자

    @OneToMany(mappedBy = "article")
    private List<ArticleTag> tags = new ArrayList<>();

    public void changeTags(ArrayList<ArticleTag> articleTag) {
        this.tags = articleTag;
    }

    // 게시글 제목 수정 메소드
    public void changeTitle(String title) {
        this.title = title;
    }

    // 게시글 원인 수정 메소드
    public void changeCause(String cause) {
        this.cause = cause;
    }

    // 게시글 해결방법 수정 메소드
    public void changeSolution(String solution) {
        this.solution = solution;
    }

    // 게시글 태그 수정 메소드
    public void changeTag(List<ArticleTag> articleTags) {
        this.tags.clear();
        this.tags.addAll(articleTags);
    }
}
