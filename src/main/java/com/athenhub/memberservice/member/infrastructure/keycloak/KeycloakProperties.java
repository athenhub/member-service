package com.athenhub.memberservice.member.infrastructure.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakProperties {

  private String serverUrl;
  private String realm;
  private String clientId;
  private String clientSecret;
}
