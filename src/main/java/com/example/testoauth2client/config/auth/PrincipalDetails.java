package com.example.testoauth2client.config.auth;

import com.example.testoauth2client.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 session을 만들어 준다. (Security ContextHolder 에 session 정보를 저장한다.)
// 오브텍트 타입 => Authentication 타입 객체로 정해져 있다.
// Authentication 안에 User 정보가 있어야 된다.
// User 오브텍트 타입 => UserDetails 타입 객체로 정의되 있다.

// Security Session => Authentication => UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                System.out.println("getAuthority : " + user.getRole());

                return user.getRole();
            }
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

    // 계정 만료?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 너무 오래 사용?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화?
    @Override
    public boolean isEnabled() {
        // 우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로함.
        // 현재시간 - 로긴시간 => 1년을 초과하면 return false;

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
