package com.athenhub.memberservice.member.infrastructure.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keycloak Admin Client 설정을 담당하는 설정 클래스.
 *
 * <p>애플리케이션에서 Keycloak Admin API를 호출하기 위한 {@link Keycloak} 클라이언트를 생성한다. 설정 값은 {@link
 * KeycloakProperties}를 통해 주입받으며, 클라이언트 자격 증명 방식({@code client_credentials})으로 토큰을 발급받도록 구성한다.
 *
 * <p>생성된 {@link Keycloak} 빈은 회원 도메인의 인프라 계층에서 사용자 생성/조회/삭제 등 관리 작업을 수행할 때 재사용된다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakAdminConfig {

  /**
   * Keycloak Admin Client를 생성하는 빈 정의.
   *
   * @param properties Keycloak 서버 접속 및 클라이언트 설정 값
   * @return 구성된 {@link Keycloak} Admin Client 인스턴스
   */
  @Bean
  public Keycloak keycloak(KeycloakProperties properties) {
    return KeycloakBuilder.builder()
        .serverUrl(properties.getServerUrl())
        .realm(properties.getRealm())
        .clientId(properties.getClientId())
        .clientSecret(properties.getClientSecret())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .build();
  }
}
