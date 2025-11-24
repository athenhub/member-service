package com.athenhub.memberservice.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationId {

  @Column(name = "organization_id", nullable = false)
  private UUID id;

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
