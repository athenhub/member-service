package com.athenhub.memberservice.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationId {

  @Column(name = "organization_id", nullable = false)
  private UUID id;

  private OrganizationId(UUID id) {
    this.id = Objects.requireNonNull(id);
  }

  public static OrganizationId of(UUID id) {
    return new OrganizationId(id);
  }

  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
