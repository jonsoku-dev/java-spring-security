package com.tamastudy.jon.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration= " + userRequest.getClientRegistration()); // registrationId 로 어떤 OAuth 로 로그인되는지 확인
        System.out.println("userRequest.getAccessToken= " + userRequest.getAccessToken());
        System.out.println("userRequest.getClientRegistration.toString= " + userRequest.getClientRegistration().toString());
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code 를 리턴(OAuth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 (회원프로필 받아야함) -> 구글로부터 회원프로필을 받아준다.
        System.out.println("super.loadUser(userRequest)= " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 회원가입을 강제로 진행 (다음)
        return super.loadUser(userRequest);
    }
}
