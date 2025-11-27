package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.infrastructure.keycloak.config.KeycloakProperties;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 회원 존재 여부를 조회하는 {@link MemberExistenceChecker} 의 인프라스트럭처 구현체.
 *
 * <p>회원 ID, username, Slack ID 등의 식별 정보를 기반으로 JPA 리포지토리(
 * {@link MemberRepository})를 사용해 이미 등록된 회원이 존재하는지 여부를 확인한다.
 *
 * <p>도메인 계층에서는 {@link MemberExistenceChecker} 인터페이스에만 의존하며, 실제 조회
 * 로직은 이 구현체를 통해 제공된다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class MemberExistenceCheckerImpl implements MemberExistenceChecker {

  private final MemberRepository memberRepository;

  /**
   * 주어진 회원 ID(UUID)가 이미 사용 중인지 확인한다.
   *
   * <p>회원 ID가 {@code null} 인 경우에는 유효한 값이 아니라고 판단하여 {@code false} 를
   * 반환한다. 유효한 UUID 인 경우에는 이를 {@link MemberId} 값 객체로 감싸서 리포지토리를
   * 통해 존재 여부를 조회한다.
   *
   * @param memberId 중복 여부를 확인할 회원 UUID
   * @return 해당 ID를 가진 회원이 존재하면 {@code true}, 그렇지 않으면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean isMemberIdAlreadyUsed(UUID memberId) {
    if (memberId == null) {
      return false;
    }
    // MemberId 는 VO 이므로 UUID 를 감싸서 조회
    return memberRepository.existsById(MemberId.of(memberId));
  }

  /**
   * 주어진 username 이 이미 사용 중인지 확인한다.
   *
   * <p>username 이 {@code null} 인 경우에는 유효한 값이 아니라고 판단하여 {@code false} 를
   * 반환한다. 유효한 username 인 경우에는 리포지토리를 통해 동일 username 을 가진 회원이
   * 존재하는지 조회한다.
   *
   * @param username 중복 여부를 확인할 계정 ID(username)
   * @return 동일 username 을 가진 회원이 존재하면 {@code true}, 그렇지 않으면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean isUsernameAlreadyUsed(String username) {
    if (username == null) {
      return false;
    }
    return memberRepository.existsByUsername(username);
  }

  /**
   * 주어진 Slack ID 가 이미 사용 중인지 확인한다.
   *
   * <p>Slack ID 가 비어 있거나 공백만 포함하는 경우({@link StringUtils#hasText(String)} 가
   * {@code false})에는 유효한 값이 아니라고 판단하여 {@code false} 를 반환한다. 유효한
   * Slack ID 인 경우에는 리포지토리를 통해 동일 Slack ID 를 사용하는 회원이 존재하는지
   * 조회한다.
   *
   * @param slackId 중복 여부를 확인할 Slack ID
   * @return 동일 Slack ID 를 사용하는 회원이 존재하면 {@code true}, 그렇지 않으면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean isSlackIdAlreadyUsed(String slackId) {
    if (!StringUtils.hasText(slackId)) {
      return false;
    }
    return memberRepository.existsBySlackId(slackId);
  }
}
