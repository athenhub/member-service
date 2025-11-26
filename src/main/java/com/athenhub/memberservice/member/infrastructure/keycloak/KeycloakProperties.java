package com.athenhub.memberservice.member.infrastructure.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Keycloak Admin Client 설정 값을 바인딩하는 프로퍼티 클래스.
 *
 * <p><code>keycloak.admin</code> 프리픽스를 가진 설정 값을 주입받아 {@link org.keycloak.admin.client.Keycloak}
 * 클라이언트 생성 시 사용한다. 예시 구성:
 *
 * <pre>{@code
 * keycloak:
 *   admin:
 *     server-url: http://localhost:8080
 *     realm: athenhub
 *     client-id: athenhub-admin
 *     client-secret: your-secret
 * }</pre>
 *
 * <ul>
 *   <li>{@code serverUrl} — Keycloak 서버의 기본 URL
 *   <li>{@code realm} — 관리 대상 Realm 이름
 *   <li>{@code clientId} — Admin API 호출에 사용할 클라이언트 ID
 *   <li>{@code clientSecret} — 클라이언트 자격 증명에 사용할 시크릿 값
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakProperties {

  /** Keycloak 서버의 기본 URL (예: {@code http://localhost:8080}) */
  private String serverUrl;

  /** 관리 대상 Realm 이름 */
  private String realm;

  /** Admin API 호출에 사용할 클라이언트 ID */
  private String clientId;

  /** 클라이언트 자격 증명에 사용할 시크릿 값 */
  private String clientSecret;
}
