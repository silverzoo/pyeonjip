package com.team5.pyeonjip.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.pyeonjip.user.dto.CustomUserDetails;
import com.team5.pyeonjip.user.entity.Refresh;
import com.team5.pyeonjip.user.repository.RefreshRepository;
import com.team5.pyeonjip.user.service.ReissueService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ReissueService reissueService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //클라이언트 요청에서 email, password 추출
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 세 번째 인자는 우선 null
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        // AuthenticationManager에 Token을 넘겨서 검증을 진행한다.
        return authenticationManager.authenticate(authToken);
    }


    // 로그인 시 access & refresh 토큰 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // 토큰에 유저 정보와 role을 stream으로 받아온다.
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        // access & refresh 토큰 생성
        String access = jwtUtil.createJwt("access", email, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        // Repository에 refresh 토큰 저장
        addRefresh(email, refresh, 86400000L);

        // 응답 설정
        response.setHeader("access", access);
        response.addCookie(reissueService.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }


    // Todo: Mapper 사용해보기
    // Repository에 Refresh 토큰을 저장
    private void addRefresh(String email, String refresh, Long expiredMs) {

        // 만료일 설정
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh newRefresh = new Refresh();
        newRefresh.setEmail(email);
        newRefresh.setRefresh(refresh);
        newRefresh.setExpiration(date.toString());

        refreshRepository.save(newRefresh);
    }


    // 이메일을 사용하기 위한 정적 클래스 정의
    private static class LoginRequest {
        private String email;
        private String password;

        // getters and setters
        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

}
