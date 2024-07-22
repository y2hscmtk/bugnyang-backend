package com.winner_cat.domain.member.service;

import com.winner_cat.domain.member.dto.JoinDTO;
import com.winner_cat.domain.member.dto.LoginRequestDTO;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.global.enums.statuscode.ErrorStatus;
import com.winner_cat.global.exception.GeneralException;
import com.winner_cat.global.jwt.util.JwtUtil;
import com.winner_cat.global.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    @Transactional
    public ResponseEntity<?> login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_CORRECT);
        }

        String accessToken = jwtUtil.createJwt(member.getUsername(), member.getEmail(), member.getRole());

        // JWT 발급 성공시 Header에 삽입하여 반환
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers).body(ApiResponse.onSuccess("Bearer " + accessToken));
    }

    public ResponseEntity<?> join(JoinDTO joinDTO) {

        // 동일 username 사용자 생성 방지
        if (memberRepository.existsMemberByEmail(joinDTO.getEmail())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.onFailure(ErrorStatus.EXSISTS_MEMBER, "이미 사용중인 이메일입니다."));
        }

        Member member = Member.builder()
                .nickname(joinDTO.getNickname())
                .email(joinDTO.getEmail())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .role("ROLE_ADMIN") // 사용자 권한 설정 접두사 ROLE 작성 필요
                .build();
        memberRepository.save(member);

        return ResponseEntity.ok().body(ApiResponse.onSuccess("회원가입 성공"));
    }

}
