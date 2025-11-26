package com.athenhub.memberservice.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * member-service 애플리케이션의 Spring Security 설정을 담당하는 구성 클래스.
 *
 * <p>공통 모듈에서 제공하는 기본 보안 설정(BaseSecurityConfig)이 아닌, 이 서비스에 특화된 보안 규칙을 정의한다. <br>
 * Actuator(헬스 체크) 및 Swagger UI / OpenAPI 문서 관련 엔드포인트는 인증 없이 접근할 수 있도록 허용하고, 그 외 모든 요청은 인증을 요구한다.
 */
@Configuration
@EnableWebSecurity
public class MemberSecurityConfig {

  /**
   * member-service에 적용될 {@link SecurityFilterChain}을 구성한다.
   *
   * <p>설정 내용은 다음과 같다.
   *
   * <ul>
   *   <li>CSRF 비활성화: REST API + 토큰 기반 인증 환경을 가정하고 있으므로 사용하지 않는다.
   *   <li>Actuator 및 Swagger 관련 엔드포인트는 모두 공개(permitAll).
   *   <li>위에서 명시적으로 허용하지 않은 나머지 모든 요청은 인증을 필수로 요구한다.
   * </ul>
   *
   * @param http Spring Security의 HTTP 보안 구성을 위한 {@link HttpSecurity}
   * @return 설정이 적용된 {@link SecurityFilterChain} 인스턴스
   * @throws Exception 보안 설정 빌드 과정에서 발생할 수 있는 예외
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/actuator/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs.html",
                        "/test/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated());
    return http.build();
  }
}
