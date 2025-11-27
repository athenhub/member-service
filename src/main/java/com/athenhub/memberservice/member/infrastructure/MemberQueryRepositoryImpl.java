package com.athenhub.memberservice.member.infrastructure;

import static org.springframework.util.StringUtils.hasText;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberQueryRepository;
import com.athenhub.memberservice.member.domain.QMember;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * {@link MemberQueryRepository} 의 Querydsl 기반 구현체.
 *
 * <p>회원 목록 조회, 검색, 정렬, 페이징 기능을 담당한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Member> search(String keyword, Pageable pageable) {
    QMember m = QMember.member;

    BooleanExpression predicate = buildPredicate(keyword, m);

    // contents 쿼리
    JPAQuery<Member> contentQuery =
        queryFactory
            .selectFrom(m)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(toOrderSpecifiers(pageable.getSort(), m));

    List<Member> contents = contentQuery.fetch();

    // total count 쿼리
    Long total =
        queryFactory
            .select(m.count())
            .from(m)
            .where(predicate)
            .fetchOne();

    long totalCount = total == null ? 0L : total;

    return new PageImpl<>(contents, pageable, totalCount);
  }

  /**
   * 검색 조건 빌드.
   *
   * <p>keyword가 없으면 전체 조회, 있으면 name / slackId / organizationName 에 대해 부분 검색을 수행한다.
   */
  private BooleanExpression buildPredicate(String keyword, QMember m) {
    if (!hasText(keyword)) {
      return null; // where(null) 이면 Querydsl에서 무시됨 → 전체 조회
    }

    String value = keyword.trim();

    return m.name.containsIgnoreCase(value)
        .or(m.slackId.containsIgnoreCase(value))
        .or(m.organizationName.containsIgnoreCase(value));
  }

  /**
   * 정렬 조건 빌드.
   *
   * <p>createdAt, updatedAt 기준만 허용하고, 그 외 필드는 기본값(createdAt desc)으로 처리한다.
   */
  private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, QMember m) {
    if (sort == null || sort.isUnsorted()) {
      return new OrderSpecifier<?>[] {m.createdAt.desc()};
    }

    return sort.stream()
        .map(
            order -> {
              String property = order.getProperty();
              boolean asc = order.isAscending();

              return switch (property) {
                case "updatedAt" -> asc ? m.updatedAt.asc() : m.updatedAt.desc();
                case "createdAt" -> asc ? m.createdAt.asc() : m.createdAt.desc();
                default -> m.createdAt.desc(); // 지원하지 않는 컬럼이면 기본 정렬
              };
            })
        .toArray(OrderSpecifier[]::new);
  }
}
