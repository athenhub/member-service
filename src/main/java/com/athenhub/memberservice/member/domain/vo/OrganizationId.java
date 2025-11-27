package com.athenhub.memberservice.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 조직(허브/업체 등)의 식별자를 표현하는 값 객체(Value Object).
 *
 * <p>내부적으로 {@link UUID}를 감싸며, 회원이 속한 조직을 구분하기 위한 식별자로 사용된다. JPA에서는 {@link Embeddable}로 매핑되며, {@code
 * organization_id} 컬럼에 매핑된다.
 *
 * <p>정적 팩터리 메서드 {@link #of(UUID)}를 통해서만 인스턴스를 생성하도록 하여 null 방지 및 일관된 생성 방식을 보장한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationId {

  @Column(name = "organization_id")
  private UUID id;

  /**
   * 주어진 UUID를 기반으로 {@link OrganizationId}를 생성한다.
   *
   * @param id 조직 식별자로 사용할 UUID (null 불가)
   * @throws NullPointerException {@code id}가 null인 경우
   */
  private OrganizationId(UUID id) {
    this.id = Objects.requireNonNull(id);
  }

  /**
   * 주어진 UUID로 {@link OrganizationId} 인스턴스를 생성한다.
   *
   * @param id 조직 식별자로 사용할 UUID
   * @return 주어진 UUID를 감싼 {@link OrganizationId}
   */
  public static OrganizationId of(UUID id) {
    return new OrganizationId(id);
  }

  /**
   * 내부에 보관 중인 UUID 값을 반환한다.
   *
   * @return 조직 식별자 UUID
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
