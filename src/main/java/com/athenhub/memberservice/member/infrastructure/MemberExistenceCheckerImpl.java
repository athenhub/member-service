package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import com.athenhub.memberservice.member.infrastructure.keycloak.KeycloakProperties;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * {@link MemberExistenceChecker} 의 Keycloak + JPA 기반 구현체.
 *
 * <p>- username 중복 여부: Keycloak Realm 에서 조회 - slackId 중복 여부: member DB(JPA) 에서 조회
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class MemberExistenceCheckerImpl implements MemberExistenceChecker {

  private final KeycloakProperties keycloakProperties;
  private final Keycloak keycloak;
  private final MemberRepository memberRepository;

  /** 주어진 회원 ID를 가진 회원이 DB(p_member 테이블)에 존재하는지 확인한다. */
  @Override
  public boolean hasMember(UUID memberId) {
    if (memberId == null) {
      return false;
    }
    // MemberId 는 VO 이므로 UUID 를 감싸서 조회
    return memberRepository.existsById(MemberId.of(memberId));
  }

  /** username 이 Keycloak Realm 에 이미 존재하는지 확인한다. */
  @Override
  public boolean existsByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return false;
    }
    UsersResource users = keycloak.realm(keycloakProperties.getRealm()).users();
    // Keycloak admin client 의 username 검색 (정확히 일치하는 계정만 찾도록 true)
    List<UserRepresentation> results = users.searchByUsername(username, true);
    return !results.isEmpty();
  }

  /** slackId 가 우리 서비스 member DB 에서 이미 사용 중인지 확인한다. */
  @Override
  public boolean existsBySlackId(String slackId) {
    if (!StringUtils.hasText(slackId)) {
      return false;
    }
    return memberRepository.existsBySlackId(slackId);
  }
}
