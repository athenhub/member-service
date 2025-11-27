package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.infrastructure.keycloak.config.KeycloakProperties;
import com.athenhub.memberservice.member.domain.service.IdentityClient;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

/**
 * Keycloak Admin API를 사용해 사용자를 생성하는 {@link IdentityClient} 구현체.
 *
 * <p>도메인 계층에서 정의한 {@link IdentityClient} 포트를 실제 Keycloak과 연결해 주는 인프라스트럭처 어댑터 역할을 한다. 사용자 생성 및 비밀번호
 * 설정을 Keycloak Admin Client로 수행하며, 생성된 사용자의 ID(UUID)를 반환한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class KeycloakIdentityClient implements IdentityClient {

  private final Keycloak keycloak;
  private final KeycloakProperties properties;

  private UsersResource users() {
    return keycloak.realm(properties.getRealm()).users();
  }

  /**
   * Keycloak에 사용자를 생성하고 비밀번호를 설정한 뒤, 생성된 사용자 ID(UUID)를 반환한다.
   *
   * <p>생성 절차:
   *
   * <ol>
   *   <li>{@link UserRepresentation}을 생성하여 username, 이름, 활성화 여부를 설정한다.
   *   <li>Keycloak Admin API의 {@code users().create(user)} 호출로 사용자 생성 요청을 보낸다.
   *   <li>응답 상태 코드가 400 이상이면 예외를 발생시킨다.
   *   <li>{@link CreatedResponseUtil#getCreatedId(Response)}를 통해 생성된 사용자 ID를 조회한다.
   *   <li>{@link CredentialRepresentation}을 사용해 비밀번호를 설정한다.
   * </ol>
   *
   * @param username 생성할 사용자 아이디(username)
   * @param rawPassword 평문 비밀번호
   * @param name 사용자 이름(표시용 이름)
   * @return Keycloak에서 생성된 사용자 ID(UUID)
   * @throws IllegalStateException Keycloak 사용자 생성 요청이 실패(HTTP 4xx/5xx)한 경우
   */
  @Override
  public UUID createUser(String username, String rawPassword, String name) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(username);
    user.setEnabled(true);
    user.setEmail(username + "@athenhub.com");
    Response response = users().create(user);

    if (response.getStatus() >= 400) {
      throw new IllegalStateException("Keycloak 사용자 생성 실패: " + response.getStatus());
    }

    String userId = CreatedResponseUtil.getCreatedId(response);

    // 비밀번호 설정
    CredentialRepresentation passwordCred = new CredentialRepresentation();
    passwordCred.setTemporary(false);
    passwordCred.setType(CredentialRepresentation.PASSWORD);
    passwordCred.setValue(rawPassword);
    users().get(userId).resetPassword(passwordCred);

    return UUID.fromString(userId);
  }

  @Override
  public String getUserName(UUID userId) {
    UserRepresentation user = users().get(userId.toString()).toRepresentation();
    return user == null ? null : user.getUsername();
  }

  @Override
  public void deleteMember(UUID memberId) {
    UserRepresentation user = users().get(memberId.toString()).toRepresentation();

    user.setEnabled(false);
    users().get(memberId.toString()).update(user);
  }
}
