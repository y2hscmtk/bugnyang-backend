package com.winner_cat.domain.article.service;

import com.winner_cat.domain.article.repository.ArticleRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
}
