package com.winner_cat.domain.oauth2.service;

import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import com.winner_cat.domain.oauth2.dto.CustomOAuth2User;
import com.winner_cat.domain.oauth2.dto.GoogleResponse;
import com.winner_cat.domain.oauth2.dto.OAuth2Response;
import com.winner_cat.domain.oauth2.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else { // 다른 소셜 로그인을 사용하는 경우 작성
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 username 만들기
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Member member = memberRepository.findByUsername(username);
        UserDTO userDTO = null;
        // member가 null 인 경우 회원가입에 해당
        if (member == null) {
            Member newMember = Member.builder()
                    .username(username) // 서비스 고유 식별 아이디
                    .nickname(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();
            memberRepository.save(newMember);

            userDTO = UserDTO.builder()
                    .username(username)
                    .nickname(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();
            return new CustomOAuth2User(userDTO);

        } else { // 기존 회원인 경우 사용자 정보 업데이트
            // 네이버, 구글 서비스 특성상 사용자 정보가 업데이트 되었을 수도 있기 때문(사용자 이름 변경,등)

            member.changeEmail(oAuth2Response.getEmail());
            member.changeName(oAuth2Response.getName());
            memberRepository.save(member);

            userDTO = UserDTO.builder()
                    .username(member.getUsername())
                    .nickname(oAuth2Response.getName())
                    .role(member.getRole())
                    .build();
            return new CustomOAuth2User(userDTO);
        }
    }
}