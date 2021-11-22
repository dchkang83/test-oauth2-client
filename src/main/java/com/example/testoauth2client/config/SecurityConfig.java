package com.example.testoauth2client.config;

import com.example.testoauth2client.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// securedEnabled - Secured 어노테이션 활성화 [@Secured("ROLE_ADMIN") 이런식으로 걸수 있다.]
// prePostEnabled - PreAuthorize, PostAuthorize 어노테이션 활성화 [@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 이런식으로 걸수 있다.]
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위 경로 빼고는 모두 권한 허용

                .and()
                .formLogin()
                .loginPage("/loginForm")
//                .usernameParameter("username")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/")

                .and()
                .oauth2Login()
                .loginPage("/loginForm")

                // 구글 로그인이 완료된 뒤의 후처리가 필요함.
                // 1: 코드받기(인증), 2: 엑세스토큰(사용자정보 접속권한), 3. 사용자프로필 정보를 가져오고,
                // 4.1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
                // 4.2. (이메일, 전화번호, 이름, 아이디)
                //   ㄴ 추가정보(쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급)) 필요하다면 추가 회원가입 페이지로
                // 구글 로그인이 완료되면. Tip. 코드X, (엑세스토큰+사용자프로필정보 O)
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
        ;
    }
}
