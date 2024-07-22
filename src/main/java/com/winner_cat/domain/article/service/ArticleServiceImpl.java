package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.ArticleCreateDto;
import com.winner_cat.domain.article.dto.ArticleUpdateDto;
import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.domain.article.repository.ArticleTagRepository;
import com.winner_cat.domain.article.repository.TagRepository;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class ArticleServiceImpl implements ArticleService{
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Override
    public ResponseEntity<ApiResponse<?>> createArticle(ArticleCreateDto.Req req, String email){

        // 1. 작성자 정보 조회
        Member author = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Article article = Article.builder()
                .author(author)
                .title(req.getTitle())
                .cause(req.getCause())
                .solution(req.getSolution())
                .build();

        Article savedArticle = articleRepository.save(article);

        List<String> tagList = req.getTags();
        List<ArticleTag> articleTags = new ArrayList<>();

        for (String tagName : tagList) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));

            ArticleTag articleTag = ArticleTag.builder()
                    .article(savedArticle)
                    .tag(tag)
                    .build();
            articleTags.add(articleTagRepository.save(articleTag));
        }

        savedArticle.changeTags(new ArrayList<>(articleTags));

        // 생성된 게시글의 정보를 포함한 응답 반환
        ArticleCreateDto.CreateArticle createArticleResponse = new ArticleCreateDto.CreateArticle(savedArticle.getId(), savedArticle.getUpdatedAt());
        ApiResponse<ArticleCreateDto.CreateArticle> res = ApiResponse.onSuccess(createArticleResponse);
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ApiResponse<?>> modifyArticle(Integer articleId, ArticleUpdateDto.Req req) {
        // 게시물 검색
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));

        // 게시물 업데이트
        articleTagRepository.deleteByArticle(article); // 기존 게시글 삭제

        article.changeTitle(req.getTitle());
        article.changeCause(req.getCause());
        article.changeSolution(req.getSolution());

        // 새로운 태그 추가
        List<String> tagList = req.getTags();
        List<ArticleTag> articleTags = new ArrayList<>();

        for (String tagName : tagList) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));

            ArticleTag articleTag = ArticleTag.builder()
                    .article(article)
                    .tag(tag)
                    .build();
            articleTags.add(articleTagRepository.save(articleTag));
        }


        article.changeTag(articleTags);

        articleRepository.flush(); // 변경 사항을 데이터베이스에 즉시 적용

        // 수정된 게시글 정보 응답
        ArticleUpdateDto.UpdateArticle data = new ArticleUpdateDto.UpdateArticle(article.getUpdatedAt());
        ApiResponse<ArticleUpdateDto.UpdateArticle> res = ApiResponse.onSuccess(data);
        return ResponseEntity.ok(res);

    }
}
