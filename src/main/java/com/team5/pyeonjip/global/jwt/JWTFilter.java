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

        // 1. request에서 Authorization 헤더를 가져온다.
        String authorization = request.getHeader("Authorization");

        // 2. 헤더에 토큰이 담겨있는지, 접두사가 Bearer로 시작하는지 검증한다.
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            // 조건에 해당되면 검증에 문제가 있는 것이므로, 메서드를 종료한다.
            return;
        }

        // 3. 'Bearer ' 문자열을 제거한 순수한 토큰을 획득한다.
        String accessToken = authorization.split(" ")[1];

        // 4. 토큰이 존재하는지 확인한다.
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // 5. 토큰 만료 여부 확인. 만료시 오류를 출력하고 다음 필터로 넘기지 않는다.
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

        // 6. 토큰이 access인지 확인한다. (페이로드에 명시되어 있다.)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("not an access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰에서 email, role 값을 가져온다.
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // UserDetails에 유저 정보를 담는다.
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.valueOf(role));
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Spring Security 인증 토큰을 생성한다.
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // SecurityContextHolder에 사용자를 등록한다.
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
