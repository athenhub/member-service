package com.athenhub.memberservice.member.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 회원 식별자를 표현하는 값 객체(Value Object).
 *
 * <p>내부적으로 {@link UUID}를 감싸며, 회원 엔터티의 주 식별자(PK) 역할을 한다. JPA에서는 {@link Embeddable}로 매핑되어 엔터티의 기본 키로
 * 사용될 수 있다.
 *
 * <p>정적 팩터리 메서드 {@link #generateId()}와 {@link #of(UUID)}를 통해서만 인스턴스를 생성하도록 제한하여, null 방지 및 일관된 생성
 * 방식을 보장한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberId {

  private UUID id;

  /**
   * 무작위 UUID를 생성하여 새로운 {@link MemberId}를 생성한다.
   *
   * @return 새로 생성된 {@link MemberId}
   */
  public static MemberId generateId() {
    return of(UUID.randomUUID());
  }

  /**
   * 주어진 UUID로 {@link MemberId} 인스턴스를 생성한다.
   *
   * @param id 회원 식별자로 사용할 UUID (null 불가)
   * @return 주어진 UUID를 감싼 {@link MemberId}
   * @throws NullPointerException {@code id}가 null인 경우
   */
  public static MemberId of(UUID id) {
    return new MemberId(Objects.requireNonNull(id));
  }

  /**
   * 내부에 보관 중인 UUID 값을 반환한다.
   *
   * @return 회원 식별자 UUID
   */
  public UUID toUuid() {
    return id;
  }

  /**
   * 내부 UUID 값을 문자열로 반환한다.
   *
   * @return UUID의 문자열 표현
   */
  @Override
  public String toString() {
    return id.toString();
  }
}
