package com.team5.pyeonjip.global.jwt;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 로그아웃 경로로 요청이 왔는지 확인한다.
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            // 로그아웃 경로가 아니면 다음 필터로 넘어간다.
            filterChain.doFilter(request, response);
            return;
        }

        // 다음으로 POST 메서드인지 확인한다.
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            // 로그아웃 경로가 아니면 다음 필터로 넘어간다.
            filterChain.doFilter(request, response);
            return;
        }

        // Refresh 토큰을 가져온다.
        String refresh = null;

        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("refresh")) {

                    refresh = cookie.getValue();
                }
            }
        } catch (ExpiredJwtException e) {
            throw new GlobalException(ErrorCode.LOGOUT_MISSING_REFRESH_TOKEN);
        }

        // 토큰이 있는지 확인한다.
        if (refresh == null) {

            throw new GlobalException(ErrorCode.LOGOUT_MISSING_REFRESH_TOKEN);
        }

        // 토큰이 만료되었는지 확인한다.
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            throw new GlobalException(ErrorCode.LOGOUT_REFRESH_TOKEN_EXPIRED);
        }

        // 가져온 토큰이 Refresh 토큰인지 확인한다. (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            throw new GlobalException(ErrorCode.LOGOUT_MISSING_REFRESH_TOKEN);
        }

        // 해당 Refresh 토큰이 DB에 저장되어 있는지 확인한다.
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            throw new GlobalException(ErrorCode.LOGOUT_MISSING_REFRESH_TOKEN);
        }

        //로그아웃 진행
        //Refresh 토큰을 DB에서 제거한다. 제거하지 않으면 재발급 될 수도 있다.
        refreshRepository.deleteByRefresh(refresh);

        // Cookie에 설정된 Refresh 토큰을 제거하는 과정.
        // 응답에 빈 쿠키를 넣음으로 제거한다.
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
