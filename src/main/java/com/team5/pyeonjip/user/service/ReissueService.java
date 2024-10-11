package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.global.jwt.JWTUtil;
import com.team5.pyeonjip.user.entity.Refresh;
import com.team5.pyeonjip.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    // Todo: 예외처리 추가하기
    // Access 토큰 재발급을 위한 컨트롤러
    public void reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        // Refresh 토큰을 가져온다.
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        // Refresh 토큰이 없는 경우
        if (refreshToken == null) {

            return;
            // throw new RefreshTokenNotFoundException("Refresh token is null.");
        }

        // Refresh 토큰이 만료되었는지 확인
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            return;
            // throw new RefreshTokenExpiredException("Refresh token is expired.");
        }

        // Refresh 토큰인지 확인 (발급시 페이로드에 명시된다)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {

            return;
            // throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        // Refresh 토큰이 DB에 저장되어 있는지 확인
        boolean isExist = refreshRepository.existsByRefresh(refreshToken);

        if (!isExist) {

            return;
            // throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        /* 여기까지 Refresh 토큰 검증 로직 */

        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // Access, Refresh JWT를 새로 생성.
        String newAccessToken = jwtUtil.createJwt("access", email, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, role, 86400000L);

        // Refresh 토큰을 생성한 후, DB에 저장된 기존의 토큰은 삭제하고 새로운 토큰을 저장한다.
        refreshRepository.deleteByRefresh(refreshToken);
        addRefresh(email, newRefreshToken, 86400000L);

        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));
    }


    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);

        // https 통신 시
        // cookie.setSecure(true);

        // 쿠키가 적용될 범위
        // cookie.setPath("/");

        // js 등에서 쿠키에 접근하지 못하도록.
        cookie.setHttpOnly(true);

        return cookie;
    }


    private void addRefresh(String email, String refresh, Long expiredMs) {

        // 만료일 설정
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh newRefresh = new Refresh();
        newRefresh.setEmail(email);
        newRefresh.setRefresh(refresh);
        newRefresh.setExpiration(date.toString());

        refreshRepository.save(newRefresh);
    }
}
