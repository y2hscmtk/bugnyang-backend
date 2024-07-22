package com.winner_cat.domain.scream.service;

import com.winner_cat.domain.scream.repository.ScreamRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Builder
public class ScreamServiceImpl implements ScreamService {
    private final ScreamRepository screamRepository;
}
