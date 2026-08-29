package com.matdang.seatdang.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 비밀번호 인코더 빈.
 *
 * SecurityConfig 안에 두면 순환 참조가 생긴다.
 *   SecurityConfig -> CustomOAuth2UserService -> BCryptPasswordEncoder(= SecurityConfig가 정의) -> SecurityConfig
 * 인코더를 별도 설정으로 분리하면 SecurityConfig와 CustomOAuth2UserService가
 * 각자 이 클래스를 바라보게 되어 고리가 끊어진다.
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
