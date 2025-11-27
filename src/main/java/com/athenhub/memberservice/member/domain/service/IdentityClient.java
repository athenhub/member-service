package com.athenhub.memberservice.member.domain.service;

import java.util.UUID;

/**
 * 인증 시스템(현재는 Keycloak)을 추상화한 포트 인터페이스.
 *
 * <p>회원 도메인은 이 인터페이스에만 의존하며, 구체적인 인증 서버(Keycloak 등)에 대해서는 알지 못한다. 실제 연동 로직은 인프라스트럭처 계층의 구현체에서 담당한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public interface IdentityClient {

  /**
   * 인증 시스템에 사용자를 생성하고, 생성된 사용자의 식별자(UUID)를 반환한다.
   *
   * @param username 생성할 사용자 계정 ID
   * @param rawPassword 평문 비밀번호
   * @param name 사용자 이름(표시용 이름)
   * @return 외부 인증 시스템에서 발급된 사용자 ID(UUID)
   */
  UUID createUser(String username, String rawPassword, String name);

  String getUserName(UUID userId);

  void deleteMember(UUID memberId);
}
