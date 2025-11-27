package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberStatus;
import java.util.UUID;

/**
 * 회원 승인 결과 응답 DTO.
 *
 * @author 박성준
 * @since 1.0.0
 */
public record MemberApproveResponse(
    UUID memberId,
    String name,
    String username,
    String slackId,
    MemberStatus status) {

  public static MemberApproveResponse from(Member member) {
    return new MemberApproveResponse(
        member.getId().toUuid(),
        member.getName(),
        member.getUsername(),
        member.getSlackId(),
        member.getStatus());
  }
}
