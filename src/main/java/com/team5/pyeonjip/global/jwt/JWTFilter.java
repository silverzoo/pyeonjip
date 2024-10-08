package com.team5.pyeonjip.global.jwt;

import com.team5.pyeonjip.user.dto.CustomUserDetails;
import com.team5.pyeonjip.user.entity.Role;
import com.team5.pyeonjip.user.entity.User;
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


@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
// 요청에 대해 한 번만 동작하는 OncePerRequestFilter를 상속

    private final JWTUtil jwtUtil;


    // 필수 구현 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 활용
        String authorization = request.getHeader("Authorization");

        // Todo: 디버깅용
//        System.out.println(authorization);
//        if (authorization == null) {
//            System.out.println("authorization null");
//        }
//        if (!authorization.startsWith("Bearer ")) {
//            System.out.println("not starts with Bearer");
//        }

//      1. Authorization 헤더를 검증한다.
//      헤더에 토큰이 담겨있는지, 접두사가 bearer로 시작하는지 검증한다.
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            // 조건에 해당되면 검증에 실패한 것이므로, 메서드를 종료한다.
            return;
        }

//      2. 토큰이 유효한지 소멸 시간을 검증한다.
//      bearer 문자열 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

//      토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            filterChain.doFilter(request, response);

            // 조건에 해당되면 검증에 실패한 것이므로, 메서드를 종료한다.
            return;
        }

//      3. 유효성 검증에 성공하면 토큰에서 정보를 가져온다.
        // Todo: getEmail, getRole 잘 돌아가는지 확인. 기존에는 role을 String 형식으로 받았음.
        String email = jwtUtil.getEmail(token);
        Role role = Role.valueOf(jwtUtil.getRole(token).toUpperCase());
        //String role = jwtUtil.getRole(token);

        // user를 생성하여 값을 set
        // Todo: Setter 사용을 최소화할 수 있을까?
        User user = new User();
        user.setEmail(email);
        user.setPassword("temppassword"); // 임시 비밀번호.
                                          // 비밀번호가 토큰에 담기게 되면 매 요청 시 DB에서 비밀번호를 조회해야 된다.
        // Todo: 원래 String 형식으로 role을 설정하는데.. 오버로딩 해도 될까?
        user.setRole(role);

        // UserDetails에 유저 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Spring Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // SecurityContextHolder에 사용자 등록 -> 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
