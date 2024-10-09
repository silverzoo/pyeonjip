package com.team5.pyeonjip.global.jwt;

import com.team5.pyeonjip.user.dto.CustomUserDetails;
import com.team5.pyeonjip.user.entity.Role;
import com.team5.pyeonjip.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;


@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
// 요청에 대해 한 번만 동작하는 OncePerRequestFilter를 상속

    private final JWTUtil jwtUtil;


    // 필수 구현 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼낸다.
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘긴다.
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 토큰이 있다면,

        // 토큰 만료 여부 확인. 만료시 오류를 출력하고 다음 필터로 넘기지 않는다.
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인한다. (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // email, role 값을 획득
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = new User();
        user.setEmail(email);
        user.setRole(Role.valueOf(role));
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
