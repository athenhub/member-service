package com.athenhub.memberservice.member.domain.service;

import java.util.UUID;

/** 외부 인증 시스템(현재는 Keycloak)을 추상화한 포트. 도메인은 이 인터페이스만 알고, Keycloak은 모른다. */
public interface IdentityClient {

  /** 외부 인증 시스템에 사용자를 생성하고 그 ID(UUID)를 반환한다. */
  UUID createUser(String username, String rawPassword, String name);
}
