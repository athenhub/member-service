package com.athenhub.memberservice.member.domain.service;

import java.util.UUID;

/**
 * 회원 존재 여부 및 중복 여부를 확인하는 도메인 서비스 인터페이스.
 *
 * <p>이 인터페이스는 회원 도메인에서 다음과 같은 검증을 위해 사용된다:
 *
 * <ul>
 *   <li>특정 ID를 가진 회원이 실제로 존재하는지 확인
 *   <li>회원 가입 시 username 이 이미 사용 중인지 확인
 *   <li>회원 가입 시 Slack ID 가 이미 사용 중인지 확인
 * </ul>
 *
 * <p>구현체는 보통 저장소(Repository)를 조회하여 실제 존재 여부를 판단하며, 도메인 계층에서는 이
 * 인터페이스에만 의존함으로써 인프라 세부 구현(예: JPA, MyBatis 등)에 대한 결합을 줄인다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public interface MemberExistenceChecker {

  /**
   * 주어진 회원 ID를 가진 회원이 존재하는지 확인한다.
   *
   * @param memberId 존재 여부를 확인할 회원의 고유 ID
   * @return 회원이 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  boolean hasMember(UUID memberId);

  /**
   * 주어진 username 이 이미 사용 중인지 확인한다.
   *
   * @param username 중복 여부를 확인할 회원 아이디(username)
   * @return 해당 username 을 사용하는 회원이 이미 존재하면 {@code true}, 아니면 {@code false}
   */
  boolean existsByUsername(String username);

  /**
   * 주어진 Slack ID 가 이미 사용 중인지 확인한다.
   *
   * @param slackId 중복 여부를 확인할 Slack ID
   * @return 해당 Slack ID 를 사용하는 회원이 이미 존재하면 {@code true}, 아니면 {@code false}
   */
  boolean existsBySlackId(String slackId);
}
