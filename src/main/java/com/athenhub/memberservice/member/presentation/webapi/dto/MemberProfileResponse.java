package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MemberProfileResponse(
  UUID id,
  String name,
  String username,
  String slackId,
  String role,
  String status,
  String organizationType,
  String organizationName,
  boolean isActivated
  ) {

  public static MemberProfileResponse from(Member member, String username) {
    UUID userId = member.getId().toUuid();

    return MemberProfileResponse.builder()
        .id(userId)
        .name(member.getName())
        .username(username)
        .slackId(member.getSlackId())
        .role(member.getRole().name())
        .status(member.getStatus().name())
        .organizationType(member.getOrganizationType().name())
        .organizationName(member.getOrganizationName())
        .isActivated(!member.isDeleted() && member.getStatus().equals(MemberStatus.ACTIVATED))
        .build();
  }
}
