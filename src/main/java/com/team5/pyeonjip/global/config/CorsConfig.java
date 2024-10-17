package com.team5.pyeonjip.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 허용할 Origin 설정
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 클라이언트가 서버로 보낼 때 허용되는 HTTP 헤더의 목록을 정의 현재는 모든 헤더 허용
                .exposedHeaders("Authorization")
                .allowCredentials(true); // 클라이언트가 서버와 교환하는 요청에서 쿠키 또는 인증 관련 정보를 포함할 수 있게 허용

    }
}
