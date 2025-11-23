package com.athenhub.memberservice.member.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberId {

  private UUID id;

  public static MemberId generateId() {
    return of(UUID.randomUUID());
  }

  public static MemberId of(UUID id) {
    return new MemberId(Objects.requireNonNull(id));
  }

  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
