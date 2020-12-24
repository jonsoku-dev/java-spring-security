package com.tamastudy.jon.config.auth;

import com.tamastudy.jon.entity.User;
import com.tamastudy.jon.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login") 을 걸어놨기 때문에,
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행.
@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Security session(내부 Authentication(내부 UserDetails (PrincipalDetails <loadUserByUsername return 값>))))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
