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
        System.out.println("userRequest.getClientRegistration= " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken= " + userRequest.getAccessToken());
        System.out.println("userRequest.getClientRegistration.toString= " + userRequest.getClientRegistration().toString());
        System.out.println("super.loadUser(userRequest)= " + super.loadUser(userRequest).getAttributes());
        // 회원가입을 강제로 진행 (다음)
        return super.loadUser(userRequest);
    }
}
