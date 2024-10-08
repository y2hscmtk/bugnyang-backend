package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.dto.*;
import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.domain.article.repository.ArticleTagRepository;
import com.winner_cat.domain.article.repository.TagRepository;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.domain.scrap.repository.ScrapRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.response.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class ArticleServiceImpl implements ArticleService{
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ScrapRepository scrapRepository;

    /**
     * 게시글 작성
     */
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

        ArticleCreateDto.CreateArticle createArticleResponse = new ArticleCreateDto.CreateArticle(savedArticle.getId(), savedArticle.getUpdatedAt());
        ApiResponse<ArticleCreateDto.CreateArticle> res = ApiResponse.onSuccess(createArticleResponse);
        return ResponseEntity.ok(res);
    }


    /**
     * 게시글 수정
     * - 게시글 작성자와 현재 로그인한 사용자가 같은 사용자인지 확인하는 작업 작성 필요
     */
    @Override
    public ResponseEntity<ApiResponse<?>> modifyArticle(Long articleId, ArticleUpdateDto.Req req, String email) {
        // 게시물 검색
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));

        // 게시글 작성자와 로그인한 사용자의 이메일 비교
        if (!article.getAuthor().getEmail().equals(email)) {
            throw new GeneralException(ErrorStatus.ARTICLE_MEMBER_NOT_FOUND);
        }

        // 게시물 업데이트
        articleTagRepository.deleteByArticle(article); // 기존 게시글 삭제

        article.changeTitle(req.getTitle());
        article.changeCause(req.getCause());
        article.changeSolution(req.getSolution());

        // 연관관계 설정
        List<String> tagList = req.getTags();
        List<ArticleTag> articleTags = new ArrayList<>();

        for (String tagName : tagList) {
            // 데이터베이스에 존재하는 태그인지 확인
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

    /**
     * 게시글 삭제
     * 내가 작성한 게시글만 삭제가 가능하다
     */
    @Override
    public ResponseEntity<ApiResponse<?>> deleteArticle(Long articleId, String email){
        // 게시물 검색
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));

        // 게시글 작성자와 요청자의 이메일 비교
        if (!article.getAuthor().getEmail().equals(email)) {
            throw new GeneralException(ErrorStatus.ARTICLE_MEMBER_NOT_FOUND);
        }

        // 스크랩 정보 삭제
        scrapRepository.deleteByArticle(article);

        // 연관관계 매핑 제거
        articleTagRepository.deleteByArticle(article);

        // 게시글 삭제
        articleRepository.delete(article);

        ApiResponse<String> res = ApiResponse.onSuccess("게시글이 삭제되었습니다.");
        return ResponseEntity.ok(res);
    }

    /**
     * 게시글 상세 보기
     */
    @Override
    public ResponseEntity<ApiResponse<?>> getArticleDetail(String email,Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ARTICLE_NOT_FOUND));
        // 현재 로그인 중인 사용자
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<TagResponseDto> tagResponseDtoList = new ArrayList<>();
        List<ArticleTag> articleTagsList = articleTagRepository.findByArticle(article);

        // 태그 정보 추출
        for (ArticleTag articleTag : articleTagsList) {
            Tag tag = articleTag.getTag();
            tagResponseDtoList.add(
                    TagResponseDto.builder()
                            .tagName(tag.getTagName())
                            .colorCode(tag.getColorCode())
                            .build()
            );
        }
        // 스크랩 유무
        boolean isScrapped = scrapRepository.existsByMemberAndArticle(member, article);
        ArticleListDto.ArticleResponse articleResponse = ArticleListDto.ArticleResponse.builder()
                .email(article.getAuthor().getEmail()) // 게시글 작성자의 이메일
                .title(article.getTitle())
                .tags(tagResponseDtoList)
                .cause(article.getCause())
                .solution(article.getSolution())
                .updatedAt(article.getUpdatedAt())
                .isScrapped(isScrapped)
                .build();

        return ResponseEntity.ok().body(ApiResponse.onSuccess(articleResponse));
    }

    /**
     * 내가 작성한 게시글 조회(미리보기)
     */
    @Override
    public ResponseEntity<ApiResponse<?>> getMyArticles(String email, Pageable pageable) {
        // 작성자 정보 조회
        Member author = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 페이징된 게시글 조회
        Page<Article> articlePage = articleRepository.findByAuthor(author, pageable);
        int totalPages = articlePage.getTotalPages();
        List<Article> articles = articlePage.getContent();
        List<ArticlePreviewDto.AllArticlePreview> articlePreviewList = new ArrayList<>();

        for (Article article : articles) {
            // 각 게시물마다 태그 조회
            List<ArticleTag> articleTagsList = articleTagRepository.findByArticle(article);
            List<TagResponseDto> tagResponseDtoList = new ArrayList<>();

            for (ArticleTag articleTag : articleTagsList) {
                Tag tag = articleTag.getTag();
                tagResponseDtoList.add(
                        TagResponseDto.builder()
                                .tagName(tag.getTagName())
                                .colorCode(tag.getColorCode())
                                .build());
            }

            articlePreviewList.add(ArticlePreviewDto.AllArticlePreview.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .tagList(tagResponseDtoList).build());
        }
        // 최종 응답 생성
        ArticlePreviewDto.AllArticlePreviewResponse result = ArticlePreviewDto.AllArticlePreviewResponse.builder()
//                .email(author.getEmail())
                .articlePreviewList(articlePreviewList)
                .totalPages(totalPages)
                .build();

        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }

    /**
     * 전체 게시글 조회(미리보기)
     */
    @Override
    public ResponseEntity<?> getAllArticle(Pageable pageable) {
        // 기본 정렬 기준 설정: createdAt 속성 기준으로 내림차순 정렬
        Pageable defaultPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 1. pageable 객체를 바탕으로 전체 게시글 엔티티 조회
        Page<Article> articlePage = articleRepository.findAll(defaultPageable);
        int totalPages = articlePage.getTotalPages();
        // 2. 반환 DTO 생성 및 반환
        List<ArticlePreviewDto.AllArticlePreview> resultDtoList = new ArrayList<>();
        for (Article article : articlePage.getContent()) {
            List<TagResponseDto> tagList = new ArrayList<>();
            // 태그 목록들 얻어와서 반환 DTO에 삽입
            article.getTags().forEach(articleTag ->
                    tagList.add(TagResponseDto.builder()
                            .tagName(articleTag.getTag().getTagName())
                            .colorCode(articleTag.getTag().getColorCode())
                            .build()));
            ArticlePreviewDto.AllArticlePreview allArticlePreviewResponseDto
                    = ArticlePreviewDto.AllArticlePreview.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .tagList(tagList)
                    .build();
            resultDtoList.add(allArticlePreviewResponseDto);
        }
        ArticlePreviewDto.AllArticlePreviewResponse result = ArticlePreviewDto.AllArticlePreviewResponse.builder()
                .totalPages(totalPages)
                .articlePreviewList(resultDtoList)
                .build();
        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }

    /**
     * 태그로 게시글 조회 - 미리보기
     */
    @Override
    public ResponseEntity<?> getArticleByTag(String tagName, Pageable pageable) {
        // 1. 해당하는 태그가 실존하는지 확인한다.
        Tag tagEntity = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));
        // 2. ArticleTag 레파지토리에서 연관관계 정보를 바탕으로 해당하는 게시글들을 페이지로 얻어온다.
        Page<ArticleTag> articleTagPage
                = articleTagRepository.findArticleTagPageByTag(tagEntity, pageable);
        List<ArticleTag> articleTagList = articleTagPage.getContent();
        int totalPages = articleTagPage.getTotalPages();
        // 3. 반환 DTO 생성 후 반환
        List<ArticlePreviewDto.AllArticlePreview> resultDtoList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
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
            resultDtoList.add(result);
        }
        ArticlePreviewDto.AllArticlePreviewResponse result = ArticlePreviewDto.AllArticlePreviewResponse.builder()
                .totalPages(totalPages)
                .articlePreviewList(resultDtoList)
                .build();

        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }

    /**
     * 태그로 내가 작성한 게시글 조회
     */
    @Override
    public ResponseEntity<?> getMyArticleByTag(String email, String tagName, Pageable pageable) {
        // 1. 사용자 정보 얻어오기
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        // 2. 태그 정보 얻어오기
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));
        // 3. 사용자와 태그 정보 바탕으로 게시글 페이징 얻어오기
        Page<ArticleTag> myArticleTagPageByTag = articleTagRepository.findMyArticleTagPageByTag(member, tag, pageable);

        // 4. DTO 생성 및 반환
        int totalPages = myArticleTagPageByTag.getTotalPages();
        List<ArticlePreviewDto.AllArticlePreview> articlePreviewList = new ArrayList<>();
        for (ArticleTag articleTag : myArticleTagPageByTag.getContent()) {
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
        ArticlePreviewDto.AllArticlePreviewResponse result = ArticlePreviewDto.AllArticlePreviewResponse.builder()
                .totalPages(totalPages)
                .articlePreviewList(articlePreviewList)
                .build();
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    /**
     * 태그로 추천 게시글 반환
     */
    @Override
    public ResponseEntity<?> getArticleRecommendByTag(String tagName, Pageable pageable) {
        // 1. 해당하는 태그가 실존하는지 확인한다.
        Tag tagEntity = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAG_NOT_FOUND));
        // 2. ArticleTag 레파지토리에서 연관관계 정보를 바탕으로 해당하는 게시글들을 페이지로 얻어온다.
        Page<ArticleTag> articleTagPage
                = articleTagRepository.findArticleTagPageByTag(tagEntity, pageable);
        List<ArticleTag> articleTagList = articleTagPage.getContent();
        // 3. 반환 DTO 생성
        List<ArticlePreviewDto.TagArticlePreview> articlePreviewList = new ArrayList<>();
        int totalPages = articleTagPage.getTotalPages(); // 총 페이지 수
        for (ArticleTag articleTag : articleTagList) {
            Article article = articleTag.getArticle();
            articlePreviewList.add(ArticlePreviewDto.TagArticlePreview.builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .cause(article.getCause())
                    .build());
        }
        ArticlePreviewDto.TagArticlePreviewResponse result = ArticlePreviewDto.TagArticlePreviewResponse.builder()
                .totalPages(totalPages)
                .articlePreviewList(articlePreviewList)
                .build();
        return ResponseEntity.ok().body(ApiResponse.onSuccess(result));
    }

    /**
     * 오늘 해결한 총 에러와, 상위 태그 4개 반환
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getTodayFixErrorInfo() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.toLocalDate().atStartOfDay();
        List<Article> todayArticles = articleRepository.findAllByCreatedAtBetween(startTime, endTime);
        long totalCount = todayArticles.size();
        
        // 가장 많이 작성된 태그 순으로 정렬
        List<Object[]> topTags = articleTagRepository.findTopTagsByArticles(todayArticles);
        // 반환 DTO 생성
        List<TodayErrorDto.ErrorDto> top4Articles = new ArrayList<>();
        topTags.stream().limit(4).forEach(tag -> {
            top4Articles.add(TodayErrorDto.ErrorDto.builder()
                    .tagName((String) tag[0])
                    .count(((Number) tag[1]).longValue())
                    .build());
        });
        TodayErrorDto.ErrorCount result = TodayErrorDto.ErrorCount.builder()
                .totalCount(totalCount)
                .ranking(top4Articles)
                .build();

        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }
}
