package com.tamastudy.jon.controller;

import com.tamastudy.jon.config.auth.PrincipalDetails;
import com.tamastudy.jon.entity.User;
import com.tamastudy.jon.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View 를 Return 하겠다 !
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody
    String loginTest(
            // 1. 첫번째 방법
            Authentication authentication,
            // 2. 세션 정보에 접근할 수 있다.
//                     @AuthenticationPrincipal UserDetails userDetails
            @AuthenticationPrincipal PrincipalDetails userDetails
    ) { // DI (의존성주입)
        /**
         *  1 번째 유저 정보 찾는 방법 - Authentication authentication 으로
         */
        System.out.println("/test/login start ======================");
        // 원래 UserDetails로 다운캐스팅을 해야하지만, PrincipalDetails가 UserDetails를 implements 하고 있기 때문에 가능
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser());

        /**
         * ２번째 유저 정보 찾는 방법 -  @AuthenticationPrincipal PrincipalDetails userDetails (어노테이션으로)
         */
        System.out.println("userDetails = " + userDetails.getUser());
        System.out.println("/test/login end ======================");
        return "세션정보확인하기";
    }

    public IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody
    String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth
    ) { // DI (의존성주입)
        System.out.println("/test/oauth/login start ======================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // PrincipalDetailsService 의 super.loadUser(userRequest).getAttributes() 와 동일한 값
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oauth.getAttributes() = " + oauth.getAttributes());
        System.out.println("/test/oauth/login end ======================");
        return "OAuth 세션정보확인하기";
    }

    @GetMapping({"", "/"})
    public @ResponseBody
    String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) but 생략가능 !
        return "index";
    }

    /**
     * OAuth 로 로그인을 해도 PrincipalDetails
     * 일반 로그인을 해도 PrincipalDetails
     * 다운 캐스팅 하지 않아도 되서 편하다.
     */
    @GetMapping("/user")
    public @ResponseBody
    String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    @GetMapping("/loginForm") // SecurityConfig 를 만들고 나서부터 작동하지 않음.
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user = " + user);
        // 회원가입
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // SecureConfig 에 일일히 등록하지 않아도 되어서 편하다.
    @GetMapping("/info")
    public @ResponseBody
    String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')") // 여러개의 role 을 정하고싶을 때
    @GetMapping("/data")
    public @ResponseBody
    String data() {
        return "데이터정보";
    }
}
