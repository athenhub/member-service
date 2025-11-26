package com.athenhub.memberservice.member.domain;

import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * {@link Member} 애그리거트를 위한 Spring Data 리포지토리 인터페이스.
 *
 * <p>회원 저장, 조회 및 중복 여부 확인 기능을 제공한다. 도메인 계층에서는 이 인터페이스에만 의존하고, 실제 구현체는 Spring Data JPA가 런타임에 동적으로
 * 생성한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public interface MemberRepository extends Repository<Member, MemberId> {

  /**
   * 회원을 저장하거나 수정한다.
   *
   * @param member 저장할 회원 엔티티
   * @return 저장된 회원 엔티티
   */
  Member save(Member member);

  /**
   * 주어진 ID를 가진 회원을 조회한다.
   *
   * @param memberId 조회할 회원 ID 값 객체
   * @return 회원이 존재하면 {@link Optional}에 담아 반환하고, 존재하지 않으면 빈 {@link Optional}
   */
  Optional<Member> findById(MemberId memberId);

  /**
   * 주어진 ID를 가진 회원이 존재하는지 확인 한다.
   *
   * @param memberId 존재 여부를 확인할 회원 ID 값 객체
   * @return 회원이 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  boolean existsById(MemberId memberId);

  /**
   * 주어진 username 을 사용하는 회원이 존재하는지 확인한다.
   *
   * @param username 중복 여부를 확인할 회원 아이디(username)
   * @return 해당 username 을 사용하는 회원이 존재하면 {@code true}, 아니면 {@code false}
   */
  boolean existsByUsername(String username);

  /**
   * 주어진 Slack ID 를 사용하는 회원이 존재하는지 확인한다.
   *
   * @param slackId 중복 여부를 확인할 Slack ID
   * @return 해당 Slack ID 를 사용하는 회원이 존재하면 {@code true}, 아니면 {@code false}
   */
  boolean existsBySlackId(String slackId);
}
