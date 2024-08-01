package com.winner_cat.init.article;

import com.winner_cat.domain.article.entity.Article;
import com.winner_cat.domain.article.entity.ArticleTag;
import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.article.repository.ArticleRepository;
import com.winner_cat.domain.article.repository.ArticleTagRepository;
import com.winner_cat.domain.article.repository.TagRepository;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class ArticleDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 회원 생성
//        Member member1 = createMember("username1","testuser@example.com","1234", "USER1", "ROLE_ADMIN" );
//        Member member2 = createMember("username2","testuser2@example.com","1234", "USER2", "ROLE_USER");

        Member member1 = memberRepository.findByUsername("username1");
        Member member2 = memberRepository.findByUsername("username2");

        // 태그 생성
        Tag tagJava = tagRepository.findByTagName("Java").get();
        Tag tagSwift = tagRepository.findByTagName("Swift").get();
        Tag tagiOS = tagRepository.findByTagName("iOS").get();
        Tag tagPython = tagRepository.findByTagName("Python").get();

        // 게시글 생성
        Article member1Article1
                = createArticle("제목1", "원인1", "해결법1", member1);
        Article member1Article2
                = createArticle("제목11", "원인11", "해결법11", member1);
        Article member2Article1
                = createArticle("제목2", "원인2", "해결법2", member2);
        Article member2Article2
                = createArticle("제목22", "원인22", "해결법22", member2);
        Article member2Article3
                = createArticle("제목3", "원인3", "해결법3", member2);
        Article member2Article4
                = createArticle("제목33", "원인33", "해결법33", member2);
        Article member2Article5
                = createArticle("제목4", "원인4", "해결법4", member2);
        Article member2Article6
                = createArticle("제목44", "원인44", "해결법44", member2);

        // 게시글 - 태그 연관관계 설정
        setTagToArticle(member1Article1,tagiOS);
        setTagToArticle(member1Article1,tagSwift);

        setTagToArticle(member1Article2,tagJava);

        setTagToArticle(member2Article1,tagiOS);
        setTagToArticle(member2Article1,tagSwift);

        setTagToArticle(member2Article2,tagSwift);

        setTagToArticle(member2Article3,tagiOS);
        setTagToArticle(member2Article3,tagSwift);

        setTagToArticle(member2Article4,tagPython);

        setTagToArticle(member2Article5,tagPython);
        setTagToArticle(member2Article5,tagSwift);
        setTagToArticle(member2Article5,tagiOS);

        setTagToArticle(member2Article6,tagPython);

    }

    public Member createMember(String username, String email, String password, String nickname, String role) {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(role)
                .build();
        return memberRepository.save(member);
    }

    public Article createArticle(String title, String cauese, String solution, Member author) {
        Article article = Article.builder()
                .title(title)
                .cause(cauese)
                .solution(solution)
                .author(author)
                .build();
        return articleRepository.save(article);
    }

    public Tag createTag(String tagName,String colorCode) {
        Tag tag = Tag.builder()
                .tagName(tagName)
                .colorCode(colorCode)
                .build();
        return tagRepository.save(tag);
    }

    public void setTagToArticle(Article article, Tag tag) {
        ArticleTag build = ArticleTag.builder()
                .article(article)
                .tag(tag)
                .build();
        articleTagRepository.save(build);
    }
}
