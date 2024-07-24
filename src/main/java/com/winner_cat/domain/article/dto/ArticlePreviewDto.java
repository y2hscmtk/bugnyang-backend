package com.winner_cat.domain.article.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 아이디, 제목, 태그
@Data
@Builder
public class ArticlePreviewDto {
    private Long articleId;
    private String title;
    @Builder.Default
    private List<String> tagList = new ArrayList<>();
}
