package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import com.athenhub.memberservice.member.domain.vo.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@link MemberExistenceChecker} 의 JPA 기반 구현체.
 *
 * <p>회원의 존재 여부 및 username, Slack ID 중복 여부를 확인하기 위해
 * {@link MemberRepository} 를 사용하여 데이터베이스를 조회한다. 도메인 계층에서는 이 구현체가
 * 아닌 {@link MemberExistenceChecker} 인터페이스에만 의존함으로써 인프라스트럭처 세부 구현으로부터
 * 분리된다.
 *
 * @author 사용자
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class MemberExistenceCheckerImpl implements MemberExistenceChecker {

  private final MemberRepository memberRepository;

  /**
   * 주어진 회원 ID를 가진 회원이 존재하는지 확인한다.
   *
   * <p>회원 ID가 {@code null} 인 경우에는 존재하지 않는 것으로 간주하고 {@code false} 를 반환한다.
   *
   * @param memberId 존재 여부를 확인할 회원의 고유 ID
   * @return 회원이 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  @Override
  public boolean hasMember(UUID memberId) {
    if (memberId == null) {
      return false;
    }
    // MemberId 가 VO 이므로 UUID를 감싸서 조회한다.
    return memberRepository.existsById(MemberId.of(memberId));
  }

  /**
   * 주어진 username 이 이미 사용 중인지 확인한다.
   *
   * <p>username 이 {@code null} 이거나 공백 문자열인 경우에는 중복이 아닌 것으로 간주하고 {@code false}
   * 를 반환한다.
   *
   * @param username 중복 여부를 확인할 회원 아이디(username)
   * @return 해당 username 을 사용하는 회원이 이미 존재하면 {@code true}, 아니면 {@code false}
   */
  @Override
  public boolean existsByUsername(String username) {
    if (username == null || username.isBlank()) {
      return false;
    }
    return memberRepository.existsByUsername(username);
  }

  /**
   * 주어진 Slack ID 가 이미 사용 중인지 확인한다.
   *
   * <p>Slack ID 가 {@code null} 이거나 공백 문자열인 경우에는 중복이 아닌 것으로 간주하고 {@code false} 를
   * 반환한다.
   *
   * @param slackId 중복 여부를 확인할 Slack ID
   * @return 해당 Slack ID 를 사용하는 회원이 이미 존재하면 {@code true}, 아니면 {@code false}
   */
  @Override
  public boolean existsBySlackId(String slackId) {
    if (slackId == null || slackId.isBlank()) {
      return false;
    }
    return memberRepository.existsBySlackId(slackId);
  }
}