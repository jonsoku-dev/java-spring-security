package com.tamastudy.jon.config.oauth;

import com.tamastudy.jon.config.auth.PrincipalDetails;
import com.tamastudy.jon.entity.User;
import com.tamastudy.jon.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public PrincipalOauth2UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration= " + userRequest.getClientRegistration()); // registrationId 로 어떤 OAuth 로 로그인되는지 확인
        System.out.println("userRequest.getAccessToken= " + userRequest.getAccessToken());
        System.out.println("userRequest.getClientRegistration.toString= " + userRequest.getClientRegistration().toString());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code 를 리턴(OAuth-client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 (회원프로필 받아야함) -> 구글로부터 회원프로필을 받아준다.
        System.out.println("super.loadUser(userRequest)= " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); // provider = google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; // ex) google_12321321302138123
        String password = bCryptPasswordEncoder.encode("tamastudy");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        // Authentication 객체 안에 들어갈 것이다.
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
