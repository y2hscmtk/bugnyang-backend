package com.winner_cat.domain.scrap.service;

import com.winner_cat.domain.article.dto.ArticlePreviewDto;
import com.winner_cat.domain.article.dto.TagResponseDto;
import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.domain.article.repository.ArticleTagRepository;
import com.winner_cat.domain.article.repository.TagRepository;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.domain.scrap.dto.ScrapDto;
import com.winner_cat.domain.scrap.entity.Scrap;
import com.winner_cat.domain.scrap.repository.ScrapRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

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
        scrapRepository.save(scrapInfo);

        return ResponseEntity.ok().body(ApiResponse.onSuccess("스크랩에 성공하였습니다."));
    }

    /**
     * 스크랩 미리보기 배열 형태로 반환
     */
    public ResponseEntity<?> getAllMyScrapArticles(String email, Pageable pageable) {
        // 1. 해당 회원이 실제로 존재하는지 확인
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 2. 해당 회원이 스크랩한 게시글 배열 얻어오기
        Page<Scrap> byMember = scrapRepository.findByMember(member, pageable);
        int totalPages = byMember.getTotalPages();
        List<Article> scrappedArticle = new ArrayList<>();
        // 얻어온 페이지에서 Scrap 객체 추출 -> 스크랩 객체에서 게시글 정보 추출하여 scrappedArticle에 저장
        byMember.getContent().forEach(scrap -> scrappedArticle.add(scrap.getArticle()));
        // 3. 반환 DTO 생성
        List<ScrapDto.ScrapPreviewDto> responseDTOList = new ArrayList<>();
        for (Article article : scrappedArticle) {
            List<ArticleTag> articleTags = article.getTags();
            List<TagResponseDto> tagsList = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                Tag tag = articleTag.getTag();
                tagsList.add(TagResponseDto.builder()
                                .tagName(tag.getTagName())
                                .colorCode(tag.getColorCode())
                                .build());
            }
            ScrapDto.ScrapPreviewDto result = ScrapDto.ScrapPreviewDto.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .tags(tagsList)
                    .build();
            responseDTOList.add(result);
        }
        // 최종 반환 DTO 생성
        ScrapDto.ScrapPreviewResponseDto result = ScrapDto.ScrapPreviewResponseDto.builder()
                .totalPages(totalPages)
                .scrapPreviewList(responseDTOList)
                .build();

        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }

    /**
     * 태그로 내가 스크랩한 게시글 조회(미리보기)
     */
    public ResponseEntity<?> getAllMyScrapArticlesByTag(String email, String tagName, Pageable pageable) {
        // 1. 사용자 정보 얻어오기
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        // 2. 태그 정보 얻어오기
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));
        // 3. 해당 회원이 스크랩한 태그 정보 조회
        Page<ArticleTag> scrapArticleTagPageByTag = articleTagRepository.findScrapArticleTagPageByTag(member, tag, pageable);
        int totalPages = scrapArticleTagPageByTag.getTotalPages();
        List<ArticlePreviewDto.AllArticlePreview> articlePreviewList = new ArrayList<>();
        for (ArticleTag articleTag : scrapArticleTagPageByTag.getContent()) {
            Article article = articleTag.getArticle();
            // 관련 태그들 얻어오기
            List<TagResponseDto> tagResponseDtoList = article.getTags().stream()
                    .map(at -> TagResponseDto.builder()
                            .tagName(at.getTag().getTagName())
                            .colorCode(at.getTag().getColorCode())
                            .build())
                    .collect(Collectors.toList());

            ArticlePreviewDto.AllArticlePreview result = ArticlePreviewDto.AllArticlePreview
                    .builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .tagList(tagResponseDtoList)
                    .build();
            articlePreviewList.add(result);
        }

        // 4. DTO 생성 및 반환
        ArticlePreviewDto.AllArticlePreviewResponse result = ArticlePreviewDto.AllArticlePreviewResponse.builder()
                .totalPages(totalPages)
                .articlePreviewList(articlePreviewList)
                .build();
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    /**
     * 스크랩 취소
     */
    public ResponseEntity<?> cancelScrap(String email, Long articleId) {
        // 1. 회원 정보 조회
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        // 2. 게시글 정보 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));
        // 3. 스크랩 정보 조회
        Scrap scrapInfo = scrapRepository.findScrapInfoByMemberAndArticle(member, article)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SCRAPINFO_NOT_FOUND));
        // 4. 스크랩 정보 삭제
        scrapRepository.delete(scrapInfo);
        return ResponseEntity.ok(ApiResponse.onSuccess("스크랩이 취소되었습니다."));
    }
}
