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
public class HubId {

  @Column(name = "hub_id", nullable = false)
  private UUID id;

  public static HubId of(UUID id) {
    return new HubId(id);
  }

  public UUID toUuid() {
    return id;
  }
  @Override
  public String toString() {
    return id.toString();
  }
}
