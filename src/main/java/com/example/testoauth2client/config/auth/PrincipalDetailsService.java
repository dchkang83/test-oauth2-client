package com.example.testoauth2client.config.auth;

import com.example.testoauth2client.model.User;
import com.example.testoauth2client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 이와 같이 설정되어 있음으로
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC(컨테이너)되어 있는 loadUserByUsername 함수가 실행 (규칙)
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session (내부에 Authentication[내부에 UserDetails])
    // @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username : " + username);
        User userEntity = userRepository.findByUsername(username);

        System.out.println("role : " + userEntity.getRole());

        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
