package com.tamastudy.jon.config;

import com.tamastudy.jon.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService oauth2UserService;

    public SecurityConfig(PrincipalOauth2UserService oauth2UserService) {
        this.oauth2UserService = oauth2UserService;
    }

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
//                .usernameParameter("username2") // username 을 변경하고싶을 때
                .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                /**
                 *  1. 코드 받기(인증),
                 *  2. 엑세스토큰(사용자 정보에 접근 할 수있는 권한),
                 *  3.사용자 프로필 정보를 가져오고함,
                 *  4-1. 그 정보를 토대로 회원가입을 자동으로 진행 (여기선 이렇게)
                 *  4-2. 이메일, 전화번호, 이름, 아이디 쇼핑몰 -> 집주소, 백화점몰 -> vip 등급, 일반등급
                 */
                .loginPage("/loginForm")
                .userInfoEndpoint()
                /**
                 * 구글 로그인이 완료된 후의 후처리가 필요함 (왜냐하면 구글로그인이 되도 세션이 없으니)
                 * Tip. 코드 X (엑세스토큰+사용자프로필정보 한방에 가져옴)
                 */
                .userService(oauth2UserService);
    }
}
