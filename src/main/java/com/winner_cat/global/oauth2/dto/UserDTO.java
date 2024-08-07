package com.winner_cat.global.oauth2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String role;
    private String nickname; // 서비스 사용자 이름
    private String username; // 서비스 사용자 고유 식별 값
    private String email; // 사용자 이메일
}
