package com.athenhub.memberservice.infrastructure.keycloak.api;

import com.athenhub.memberservice.application.dto.TokenInfo;
import com.athenhub.memberservice.application.service.TokenGenerateService;
import com.athenhub.memberservice.infrastructure.keycloak.config.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakTokenGenerateService implements TokenGenerateService {

  private final KeycloakProperties properties;

  @Override
  public TokenInfo generateToken(String username, String password) {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "password");
    form.add("client_id", properties.getClientId());
    form.add("client_secret", properties.getClientSecret());
    form.add("username", username);
    form.add("password", password);
    form.add("scope", "openid profile email"); // 필요한가?
    RestClient client = RestClient.create();
    ResponseEntity<TokenInfo> res =
        client
            .post()
            .uri(
                String.format(
                    "%s/realms/%s/protocol/openid-connect/token",
                    properties.getServerUrl(), properties.getRealm()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .toEntity(TokenInfo.class);

    if (res.getStatusCode().is2xxSuccessful()) {
      return res.getBody();
    }
    return null;
  }
}
