package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.global.response.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Builder
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;

    @Override
    public ResponseEntity<ApiResponse<?>> createArticle(ArticleCreateDto.Req req){
        Article article = Article.builder()
                .title(req.getTitle())
                .cause(req.getCause())
                .solution(req.getSolution())
                .build();

        // ArrayList<ArticleTag> tags = new ArrayList<>();
        //article.changeTags(tags);

        Article savedArticle = articleRepository.save(article);

        // 생성된 게시글의 정보를 포함한 응답 반환
        ArticleCreateDto.CreateArticle createArticleResponse = new ArticleCreateDto.CreateArticle(savedArticle.getId(), savedArticle.getUpdatedAt());
        ApiResponse<ArticleCreateDto.CreateArticle> res = ApiResponse.onSuccess(createArticleResponse);
        return ResponseEntity.ok(res);
    }
}
