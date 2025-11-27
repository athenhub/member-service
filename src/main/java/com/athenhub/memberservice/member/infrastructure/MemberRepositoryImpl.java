package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.QMember;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * {@link MemberRepository} 의 Querydsl 기반 구현체.
 *
 * <p>JPA {@link EntityManager} 와 {@link JPAQueryFactory} 를 활용하여 {@link Member} 엔터티에 대한
 * 저장, 조회, 존재 여부 확인 기능을 제공한다. 도메인 계층에서는 {@link MemberRepository} 인터페이스에만
 * 의존하며, 실제 구현은 인프라스트럭처 계층의 이 클래스가 담당한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final JPAQueryFactory queryFactory;

  @PersistenceContext
  private EntityManager em;

  /**
   * {@link Member} 엔터티를 영속성 컨텍스트에 저장한다.
   *
   * <p>새로 생성된 회원 엔터티를 영속 상태로 만들며, 트랜잭션 커밋 시점에 DB에 반영된다.
   *
   * @param member 저장할 회원 엔터티
   * @return 저장된 회원 엔터티
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public Member save(Member member) {
    em.persist(member);
    return member;
  }

  /**
   * 주어진 {@link MemberId} 를 가진 회원을 조회한다.
   *
   * <p>Querydsl 을 사용하여 ID 조건으로 단건 조회를 수행하며, 조회 결과가 없을 경우
   * {@link Optional#empty()} 를 반환한다.
   *
   * @param memberId 조회할 회원의 식별자 값 객체
   * @return 회원이 존재하면 해당 {@link Member} 를 감싼 {@link Optional}, 존재하지 않으면
   *     {@link Optional#empty()}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public Optional<Member> findById(MemberId memberId) {
    QMember qMember = QMember.member;
    Member member =
        queryFactory.selectFrom(qMember).where(qMember.id.eq(memberId)).fetchFirst();

    return Optional.ofNullable(member);
  }

  @Override
  public List<Member> findAllByRole(MemberRole role) {
    return List.of();
  }

  /**
   * 주어진 {@link MemberId} 를 가진 회원이 존재하는지 여부를 확인한다.
   *
   * <p>Querydsl 을 사용하여 카운트 쿼리를 수행하며, 결과가 1건 이상이면 {@code true}, 그렇지 않으면
   * {@code false} 를 반환한다.
   *
   * @param memberId 존재 여부를 확인할 회원 식별자 값 객체
   * @return 해당 ID 를 가진 회원이 하나 이상 존재하면 {@code true}, 아니면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean existsById(MemberId memberId) {
    QMember qMember = QMember.member;
    Long count =
        queryFactory
            .select(qMember.count())
            .from(qMember)
            .where(qMember.id.eq(memberId))
            .fetchFirst();
    return count != null && count > 0;
  }

  /**
   * 주어진 username 을 가진 회원이 존재하는지 여부를 확인한다.
   *
   * <p>Querydsl 을 사용하여 username 컬럼 기준으로 카운트 쿼리를 수행하며, 결과가 1건 이상이면
   * {@code true}, 그렇지 않으면 {@code false} 를 반환한다.
   *
   * @param username 존재 여부를 확인할 회원 계정 ID(username)
   * @return 동일한 username 을 가진 회원이 하나 이상 존재하면 {@code true}, 아니면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean existsByUsername(String username) {
    QMember qMember = QMember.member;
    Long count =
        queryFactory
            .select(qMember.count())
            .from(qMember)
            .where(qMember.name.eq(username))
            .fetchFirst();
    return count != null && count > 0;
  }

  /**
   * 주어진 Slack ID 를 가진 회원이 존재하는지 여부를 확인한다.
   *
   * <p>Querydsl 을 사용하여 slackId 컬럼 기준으로 카운트 쿼리를 수행하며, 결과가 1건 이상이면
   * {@code true}, 그렇지 않으면 {@code false} 를 반환한다.
   *
   * @param slackId 존재 여부를 확인할 Slack ID
   * @return 동일한 Slack ID 를 가진 회원이 하나 이상 존재하면 {@code true}, 아니면 {@code false}
   * @author 박성준
   * @since 1.0.0
   */
  @Override
  public boolean existsBySlackId(String slackId) {
    QMember qMember = QMember.member;
    Long count =
        queryFactory
            .select(qMember.count())
            .from(qMember)
            .where(qMember.slackId.eq(slackId))
            .fetchFirst();
    return count != null && count > 0;
  }
}
