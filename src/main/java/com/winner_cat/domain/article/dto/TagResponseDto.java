package com.winner_cat.domain.article.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 태그 반환 DTO
 */
@Data
@Builder
public class TagResponseDto {
    private String tagName;
    private String colorCode;
}
