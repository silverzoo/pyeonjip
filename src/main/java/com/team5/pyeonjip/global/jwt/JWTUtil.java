package com.team5.pyeonjip.global.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    // secret key를 저장할 객체
    private SecretKey secretKey;

//    오류 발생.
//    public JWTUtil(@Value("{spring.jwt.secret") String secret) {
//
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
//    }

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        // HMAC SHA 알고리즘에 맞는 SecretKey를 생성한다.
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    // Todo: getEmail로 메서드명 수정해도 문제 없을지 고려해보기. 기존은 getUsername
    public String getEmail(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }


    // Todo: Role을 ENUM으로 구현했기에 문제 생길 수도 있음.
    public String getRole(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }


    // access / refresh 토큰 구별을 위한 카테고리 getter
    public String getCategory(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }


    // 토큰 만료 시간 확인
    public Date getExpirationDate(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }


    // 토큰 만료 여부 확인
    public Boolean isExpired(String token) {

        return Jwts.parser()
                // 오차 시간 허용
                .clockSkewSeconds(30)
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }


    // category는 access / refresh 토큰을 구별하기 위해 사용한다.
    // 참고: 기존에는 email이 username이었음.
    public String createJwt(String category, String email, String role, Long expiredMs) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                // 키에 대한 특정 데이터를 담는다.
                .claim("category", category)
                .claim("email", email)
                .claim("role", role)

                // 발행 시간
                .issuedAt(new Date(System.currentTimeMillis()))

                // 만료 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs))

                // secretKey로 암호화
                .signWith(secretKey)

                .compact();
    }
}
