package com.tamastudy.jon.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 only security session 을 만들어준다. (Security ContextHolder 라는 key 값에 저장)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트의 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails

import com.tamastudy.jon.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private final User user; // composition

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User 의 권한을 리턴하는 곳 !!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return null;
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
}
