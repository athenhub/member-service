package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.infrastructure.keycloak.config.KeycloakProperties;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
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
 * <p>회원 존재 여부 및 중복 여부를 다음과 같이 확인한다.
 *
 * <ul>
 *   <li>회원 존재 여부: member DB(p_member 테이블, JPA) 조회
 *   <li>username 중복 여부: Keycloak Realm 사용자 조회
 *   <li>slackId 중복 여부: member DB(JPA) 조회
 * </ul>
 *
 * <p>도메인 계층은 이 구현체가 아닌 {@link MemberExistenceChecker} 인터페이스에만 의존하며, Keycloak 및 JPA 관련 세부 사항은
 * 인프라스트럭처 계층에 캡슐화된다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class MemberExistenceCheckerImpl implements MemberExistenceChecker {

  private final KeycloakProperties keycloakProperties;
  private final Keycloak keycloak;
  private final MemberRepository memberRepository;

  /**
   * 주어진 회원 ID를 가진 회원이 DB(p_member 테이블)에 존재하는지 확인한다.
   *
   * @param memberId 존재 여부를 확인할 회원의 UUID
   * @return 회원이 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  @Override
  public boolean hasMember(UUID memberId) {
    if (memberId == null) {
      return false;
    }
    // MemberId 는 VO 이므로 UUID 를 감싸서 조회
    return memberRepository.existsById(MemberId.of(memberId));
  }

  /**
   * 주어진 username 이 Keycloak Realm 에 이미 존재하는지 확인한다.
   *
   * <p>Keycloak Admin Client의 {@link UsersResource#searchByUsername(String, Boolean)}를 사용하며, 정확히
   * 일치하는 username 만 검색하도록 설정한다.
   *
   * @param username 중복 여부를 확인할 username
   * @return 해당 username 을 가진 계정이 하나 이상 존재하면 {@code true}, 아니면 {@code false}
   */
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

  /**
   * 주어진 Slack ID 가 우리 서비스 member DB 에서 이미 사용 중인지 확인한다.
   *
   * @param slackId 중복 여부를 확인할 Slack ID
   * @return 해당 Slack ID 를 사용하는 회원이 존재하면 {@code true}, 아니면 {@code false}
   */
  @Override
  public boolean existsBySlackId(String slackId) {
    if (!StringUtils.hasText(slackId)) {
      return false;
    }
    return memberRepository.existsBySlackId(slackId);
  }
}
