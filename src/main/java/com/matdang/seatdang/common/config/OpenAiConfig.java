package com.matdang.seatdang.common.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * OpenAI 클라이언트 빈.
 *
 * 보안 설정과 아무 관계가 없는데도 SecurityConfig 안에 있었다.
 * 그래서 CakeDesignService -> OpenAiService 로 주입을 받으려던 AI 기능이
 * SecurityConfig의 순환 참조에 함께 걸려 애플리케이션이 기동하지 못했다.
 */
@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(30));
    }
}
