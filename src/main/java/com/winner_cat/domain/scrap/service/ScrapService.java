package com.winner_cat.domain.scrap.service;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.domain.scrap.entity.Scrap;
import com.winner_cat.domain.scrap.repository.ScrapRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public ResponseEntity<?> scrapArticle(String email, Long articleId) {
        // 1. 회원 조회
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 2. 게시글 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));

        // 3. 스크랩
        Scrap scrapInfo = Scrap.builder()
                .article(article)
                .member(member)
                .build();
        Scrap scrapEntity = scrapRepository.save(scrapInfo);

        return ResponseEntity.ok().body(ApiResponse.onSuccess("스크랩에 성공하였습니다."));
    }
}
