package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberStatus;
import java.util.UUID;

public record MemberRejectResponse(
    UUID memberId,
    String name,
    String username,
    String slackId,
    MemberStatus status) {

  public static MemberRejectResponse from(Member member) {
    return new MemberRejectResponse(
        member.getId().toUuid(),
        member.getName(),
        member.getUsername(),
        member.getSlackId(),
        member.getStatus());
  }
}
