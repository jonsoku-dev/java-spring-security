package com.tamastudy.jon.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 only security session 을 만들어준다. (Security ContextHolder 라는 key 값에 저장)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트의 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails

import com.tamastudy.jon.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * implements UserDetails, OAuth2User
 * UserDetails, OAuth2User 를 implements 한다.
 * Authentication 에는 UserDetails 객체와 OAuth2User 객체만 들어 올 수 있다.
 * 컨트롤러에서 사용할 때 타입을 2개 다 적어주기엔 공수가 많이 들기 때문에,
 * 하나의 부모객체를 두고, 이 안에 UserDetails, OAuth2User 객체를 두면, 부모객체만 사용하면 된다.
 * <p>
 * 물론 둘다 overriding 해야한다. 기능구현은 아래 코드를 참고
 */

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user; // composition
    private Map<String, Object> attributes;

    // 일반 로그인 시 사용하는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인 시 사용하는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User 의 권한을 리턴하는 곳 !!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(() -> {
            return user.getRole();
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 우리 사이트!! 1년동안 회원이 로그인을 하지 않으면, 휴면 계정으로 하기로 함.
        // 현재시간 - 로그인시간 => 1년을 초과하면 return false
        // 예제에서는 실제로 하지않는다.
//        user.getLoginDate();
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
