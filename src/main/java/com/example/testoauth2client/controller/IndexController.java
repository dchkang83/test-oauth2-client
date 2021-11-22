package com.example.testoauth2client.controller;

import com.example.testoauth2client.config.auth.PrincipalDetails;
import com.example.testoauth2client.model.User;
import com.example.testoauth2client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // VIEW를 리턴한다.
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody
    String testLogin(
            Authentication authentication,
//            @AuthenticationPrincipal UserDetails userDetails) { // DI (의존성주입)
            @AuthenticationPrincipal PrincipalDetails userDetails) { // DI (의존성주입)
        System.out.println("/test/login ==========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());
        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody
    String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) { // DI (의존성주입)
        System.out.println("/test/oauth/login ==========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication1 : " + oAuth2User.getAttributes());
        System.out.println("authentication2 : " + oauth.getAttributes());
        return "세션 정보 확인하기";
    }

    // localhost:8080
    // localhost:8080/
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser : " + principalDetails.getUser());
        System.out.println("principalDetails.getAttributes : " + principalDetails.getAttributes());
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

    /**
     * 로그인 페이지
     *
     * @return
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    /**
     * 회원가입 페이지
     *
     * @return
     */
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    /**
     * 회원가입
     *
     * @return
     */
    @PostMapping("/join")
    public String join(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setRole("ROLE_USER");
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";// redirect를 붙히면 controller에 loginForm 함수 호출
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody
    String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody
    String data() {
        return "데이터정보";
    }
}