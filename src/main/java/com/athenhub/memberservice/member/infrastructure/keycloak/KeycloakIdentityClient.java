package com.athenhub.memberservice.member.infrastructure.keycloak;

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

@Component
@RequiredArgsConstructor
public class KeycloakIdentityClient implements IdentityClient {

  private final Keycloak keycloak;
  private final KeycloakProperties properties;

  private UsersResource users() {
    return keycloak.realm(properties.getRealm()).users();
  }

  @Override
  public UUID createUser(String username, String rawPassword, String name) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(username);
    user.setFirstName(name);
    user.setEnabled(true);

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
}
