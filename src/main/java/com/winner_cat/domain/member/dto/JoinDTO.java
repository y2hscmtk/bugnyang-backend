package com.winner_cat.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinDTO {
    private String email; // google-test@gmail.com
    private String password;
}
