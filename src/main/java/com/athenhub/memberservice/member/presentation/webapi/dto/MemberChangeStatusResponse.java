package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberStatus;
import java.util.UUID;

public record MemberChangeStatusResponse(
    UUID memberId,
    MemberStatus status

) {

  public static MemberChangeStatusResponse from(Member member) {
    return new MemberChangeStatusResponse(
        member.getId().toUuid(),
        member.getStatus()
    );
  }
}
