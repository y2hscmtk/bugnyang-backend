package com.winner_cat.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinDTO {
    @NotNull(message = "닉네임을 적어주세요.")
    private String nickname; // 이름
    @NotNull(message = "이메일을 적어주세요.")
    @Email
    private String email; // 이메일이 로직상 아이디에 해당
    @NotNull(message = "비밀번호를 적어주세요")
    private String password;
}
